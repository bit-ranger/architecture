package com.rainyalley.architecture.util;

import net.jcip.annotations.NotThreadSafe;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author bin.zhang
 *
 * HTTP 客户端
 * 当请求符合以下条件的任何一个时, 将自动重试, 直至达到重试次数上限 {@link #setRetryTimes(int)}
 *      1.幂等的请求(GET, HEAD, PUT, DELETE, OPTIONS, TRACE)
 *      2.请求数据未完整发送
 *      3.被指定为重试的请求 {@link #post(String, Map, boolean, Header...)}
 *
 */
@NotThreadSafe
public class HttpPoolingClient extends CloseableHttpClient implements StringHttpClient {

    /**
     * 将请求标记为可重试
     */
    private final static String http_req_retry = "http.request_retry";

	private Logger logger = LoggerFactory.getLogger(getClass());



    private int connectionRequestTimeout = 5000;


    private int socketTimeout = 5000;


    private int connectTimeout = 5000;


    private int maxTotal = 1000;


    private int maxPerRoute = 200;


    private Map<String, Integer> routeMax = Collections.emptyMap();

    private int retryTimes = 3;

    private Charset charset = Charset.forName("UTF-8");


    private  CloseableHttpClient client;

    public void init(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);

        for (Map.Entry<String, Integer> entry : routeMax.entrySet()) {
            String[] hostAndPort = entry.getKey().split(":");
            HttpHost httpHost = new HttpHost(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
            // 设定目标主机的最大连接数
            connectionManager.setMaxPerRoute(new HttpRoute(httpHost), entry.getValue());
        }

        org.apache.http.client.HttpRequestRetryHandler retryHandler = new SpecificHttpRequestRetryHandler(retryTimes, false);

        client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
                .build();
    }


    @Override
    protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return client.execute(target, request, context);
    }


    protected String receiveString(final HttpUriRequest request, final HttpContext context) throws IOException{
        CloseableHttpResponse response = null;
        try {
            if(logger.isDebugEnabled()){
                logger.debug(String.format("send %s >>> %s", request, context));
            }

            response = this.execute(request, context);
            HttpEntity httpEntity = response.getEntity();
            String respString = httpEntity != null ? EntityUtils.toString(httpEntity, charset) : null;

            if(logger.isDebugEnabled()){
                logger.debug(String.format("recv %s >>> %s\n%s", request, response, respString));
            }

            int respStatus = response.getStatusLine().getStatusCode();

            if (respStatus == HttpStatus.SC_OK) {
                return respString;
            } else {
                throw new IllegalStateException(String.format("expect %s, actual %s", HttpStatus.SC_OK, respStatus));
            }
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error(String.format("error %s >>> %s", request, context), e);
            }
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


    /**
     * get请求
     * @param url url
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    @Override
    public String get(String url, Header... header) throws IOException{
        HttpGet httpGet = new HttpGet(url);
        for (Header h : header) {
            httpGet.addHeader(h);
        }

        try {
            return receiveString(httpGet, null);
        } finally {
            httpGet.releaseConnection();
        }

    }



    /**
     * post请求
     * @param url url
     * @param params UrlEncodedForm 参数对
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    @Override
    public String post(String url, Map<String, String> params, Header... header) throws IOException{
        return post(url, params, false, header);
    }


    /**
     * 可重试的post请求
     * @param url url
     * @param params UrlEncodedForm 参数对
     * @param retry true重试， false不重试
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    @Override
    public String post(String url, Map<String, String> params, boolean retry, Header... header) throws IOException {
        ShowEntityHttpPost httpPost = new ShowEntityHttpPost(url);
        httpPost.setShowEntity(true);
        httpPost.addHeader(HttpHeaders.CONTENT_ENCODING, charset.name());
        for (Header h : header) {
            httpPost.addHeader(h);
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(transferMap(params), charset);
        httpPost.setEntity(entity);
        try {
            BasicHttpContext context = new BasicHttpContext();
            if(retry){
                context.setAttribute(http_req_retry, true);
            }
            return receiveString(httpPost, context);
        } finally {
            httpPost.releaseConnection();
        }
    }


    /**
     * post请求
     * @param url url
     * @param payload String请求体，将保持原样
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    @Override
    public String post(String url, String payload, Header... header) throws IOException{
        return post(url, payload, false, header);
    }


    /**
     * post请求
     * @param url url
     * @param payload String请求体，将保持原样
     * @param retry true重试， false不重试
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    @Override
    public String post(String url, String payload, boolean retry, Header... header) throws IOException {
        ShowEntityHttpPost httpPost = new ShowEntityHttpPost(url);
        httpPost.setShowEntity(true);
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.addHeader(HttpHeaders.CONTENT_ENCODING, charset.name());
        for (Header h : header) {
            httpPost.addHeader(h);
        }

        StringEntity entity = new StringEntity(payload, charset);
        httpPost.setEntity(entity);
        try {
            BasicHttpContext context = new BasicHttpContext();
            if(retry){
                context.setAttribute(http_req_retry, true);
            }
            return receiveString(httpPost, context);
        } finally {
            httpPost.releaseConnection();
        }
    }


    private List<NameValuePair> transferMap(Map<String, String> paramMap) {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formParams;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public HttpParams getParams() {
        return client.getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return client.getConnectionManager();
    }


    /**
     * 指定的请求将重试
     */
    private static class SpecificHttpRequestRetryHandler extends StandardHttpRequestRetryHandler{

        public SpecificHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
            super(retryCount, requestSentRetryEnabled);
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            boolean retry = super.retryRequest(exception, executionCount, context);

            //如果之前已判断为可重试，则重试
            if(retry){
                return true;
            }

            if (executionCount > this.getRetryCount()) {
                //超过次数则不重试
                return false;
            }

            //如果之前判断为不可重试，此判断是否指定为重试
            return Boolean.TRUE.equals(context.getAttribute(http_req_retry));

        }
    }

    private static class ShowEntityHttpPost extends HttpPost{

        private boolean showEntity = false;

        public ShowEntityHttpPost(String uri) {
            super(uri);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());

            if (showEntity) {
                HttpEntity entity = getEntity();
                if (entity != null && entity.isRepeatable()) {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        entity.writeTo(baos);
                        Header[] encoding = getHeaders(HttpHeaders.CONTENT_ENCODING);
                        String entityString;
                        if (encoding != null && encoding.length > 0) {
                            entityString = baos.toString(encoding[0].getValue());
                        } else {
                            entityString = baos.toString();
                        }
                        sb.append(" ");
                        sb.append(entityString);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                }
            }

            return sb.toString();
        }

        public void setShowEntity(boolean showEntity) {
            this.showEntity = showEntity;
        }
    }


    /**
     * 从连接池中获取连接的时间
     * 毫秒
     * 0表示无限
     * 负数表示未定义
     * Default: {@code -1}
     *
     * 若超时, 将抛出 {@link ConnectionPoolTimeoutException}
     * 此时请求尚未发送
     */
    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }


    /**
     * 等待数据返回的时间
     * 毫秒
     * 0表示无限
     * 负数表示未定义
     * Default: {@code -1}
     *
     * 若超时, 将抛出 {@link SocketTimeoutException}
     * 此时请求已发送完成，但尚未读完目标服务器的响应数据，此时无法判断目标服务器的状态
     *
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 连接建立的时间
     * 毫秒
     * 0表示无限
     * 负数表示未定义
     * Default: {@code -1}
     *
     * 若超时, 将抛出 {@link ConnectTimeoutException}
     * 此时请求尚未发送
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 连接池最大连接数
     */
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    /**
     * 每个路由最大连接数
     */
    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    /**
     * 指定路由的最大连接数
     */
    public void setRouteMax(Map<String, Integer> routeMax) {
        this.routeMax = routeMax;
    }

    /**
     * 重试次数
     */
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     *
     * @param charset 字符集
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public Map<String, Integer> getRouteMax() {
        return routeMax;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Charset getCharset() {
        return charset;
    }
}