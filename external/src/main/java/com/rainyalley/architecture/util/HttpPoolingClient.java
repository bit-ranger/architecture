package com.rainyalley.architecture.util;

import com.rainyalley.architecture.exception.ConversionException;
import net.jcip.annotations.NotThreadSafe;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
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
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bin.zhang
 *
 * HTTP 客户端
 * 当请求符合以下条件的任何一个时, 将自动重试, 直至达到重试次数上限 {@link #setRetryTimes(int)}
 *      1.幂等的请求(GET, HEAD, PUT, DELETE, OPTIONS, TRACE)
 *      2.请求数据未完整发送
 *      3.被指定为重试的请求 {@link #post(String, Map, boolean, Header...)}
 *
 *  内部依赖是可修改的，所以非线程安全，但如果不存在并发修改成员变量的场景，则可以认为是线程安全的
 */
@NotThreadSafe
public class HttpPoolingClient extends CloseableHttpClient implements StringHttpClient {

    private final static ScheduledThreadPoolExecutor SCHEDULED = new ScheduledThreadPoolExecutor(
            1,
            new CustomizableThreadFactory("http-pooling-client-scheduled"));

    /**
     * 将请求标记为可重试
     */
    private final static String HTTP_REQ_RETRY = "http.request.retry";

    /**
     * 请求的uuid
     */
    private final static String HTTP_REQ_UUID = "http.request.uuid";
    

	private Logger logger = LoggerFactory.getLogger(getClass());



    private int connectionRequestTimeout = 5000;


    private int socketTimeout = 5000;


    private int connectTimeout = 5000;


    private int maxTotal = 1000;

    private int maxPerRoute = 200;

    private int maxIdleTimeout = 60000;

    private int cleanExpireInterval = 60000;


    private Map<String, Integer> routeMax = Collections.emptyMap();

    private int retryTimes = 3;

    private Charset charset = Charset.forName("UTF-8");

    public ContentType applicationJson = ContentType.create("application/json", charset);


    private  CloseableHttpClient client;

    public void init() throws Exception{
        applicationJson = ContentType.create("application/json", charset);

        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);

        for (Map.Entry<String, Integer> entry : routeMax.entrySet()) {
            String[] hostAndPort = entry.getKey().split(":");
            HttpHost httpHost = new HttpHost(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
            // 设定目标主机的最大连接数
            connectionManager.setMaxPerRoute(new HttpRoute(httpHost), entry.getValue());
        }

        org.apache.http.client.HttpRequestRetryHandler retryHandler = new SpecificHttpRequestRetryHandler(retryTimes, false);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).build();

        client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
                .build();

        cleanConnections(connectionManager);
    }


    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    private static X509TrustManager getX509TrustManager(){
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }

    private void cleanConnections(final PoolingHttpClientConnectionManager cm) {
        SCHEDULED.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (cm != null) {
                    cm.closeExpiredConnections();
                    cm.closeIdleConnections(maxIdleTimeout, TimeUnit.MILLISECONDS);
                    if(logger.isDebugEnabled()){
                        logger.debug(String.format("cleanConnections, current: %s", cm.getTotalStats()));
                    }
                }
            }
        }, cleanExpireInterval, cleanExpireInterval, TimeUnit.MILLISECONDS);
    }


    @Override
    protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return client.execute(target, request, context);
    }


    protected String receiveString(final HttpUriRequest request, final HttpContext context) throws IOException{
        CloseableHttpResponse response = null;
        try {
            context.setAttribute(HTTP_REQ_UUID, UUID.randomUUID());
            response = this.execute(request, context);
            HttpEntity httpEntity = response.getEntity();
            String respString = httpEntity != null ? EntityUtils.toString(httpEntity, charset) : null;
            if(logger.isDebugEnabled()){
                logger.debug(String.format("receiveString: %s >>> %s @ %s", request, respString, context));
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK ) {
                throw new IllegalStateException();
            }

            return respString;
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
                logger.error(String.format("receiveString: %s >>> %s:%s @ %s", request, e.getClass().getName(), e.getMessage(), context));
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
            return receiveString(httpGet, new BasicHttpContext());
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
                context.setAttribute(HTTP_REQ_RETRY, true);
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
        httpPost.addHeader(HttpHeaders.CONTENT_ENCODING, charset.name());
        for (Header h : header) {
            httpPost.addHeader(h);
        }

        StringEntity entity = new StringEntity(payload, applicationJson);
        httpPost.setEntity(entity);
        try {
            BasicHttpContext context = new BasicHttpContext();
            if(retry){
                context.setAttribute(HTTP_REQ_RETRY, true);
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
    private class SpecificHttpRequestRetryHandler extends StandardHttpRequestRetryHandler{

        public SpecificHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
            super(retryCount, requestSentRetryEnabled);
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            boolean retry = super.retryRequest(exception, executionCount, context);


            if(!retry){
                retry = exception instanceof NoHttpResponseException;
            }

            if(!retry){
                //如果之前判断为不可重试，此判断是否指定为重试
                retry =  Boolean.TRUE.equals(context.getAttribute(HTTP_REQ_RETRY));
            }

            if(retry){
                if(logger.isDebugEnabled()){
                    logger.debug(String.format("retryRequest: %s >>> %s, %s@%s", context.getAttribute(HTTP_REQ_UUID), executionCount, exception.getMessage(), exception.getClass().getName()));
                }
            }

            return retry;

        }
    }

    private class ShowEntityHttpPost extends HttpPost{

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
                        throw new ConversionException(String.class, entity);
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

    /**
     * 最大空闲时间
     * @param maxIdleTimeout
     */
    public void setMaxIdleTimeout(int maxIdleTimeout) {
        this.maxIdleTimeout = maxIdleTimeout;
    }

    /**
     * 清除过期连接的间隔
     * @param cleanExpireInterval
     */
    public void setCleanExpireInterval(int cleanExpireInterval) {
        this.cleanExpireInterval = cleanExpireInterval;
    }
}