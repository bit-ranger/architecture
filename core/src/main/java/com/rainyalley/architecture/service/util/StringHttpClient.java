package com.rainyalley.architecture.service.util;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * @author bin.zhang
 * 面向字符串的http client
 */
public interface StringHttpClient extends HttpClient, Closeable {

    /**
     * get请求
     * @param url url
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    String get(String url, Header... header) throws IOException;


    /**
     * post请求
     * @param url url
     * @param params UrlEncodedForm 参数对
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    String post(String url, Map<String, String> params, Header... header) throws IOException;

    /**
     * 可重试的post请求
     * @param url url
     * @param params UrlEncodedForm 参数对
     * @param retry true重试， false不重试
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    String post(String url, Map<String, String> params, boolean retry, Header... header) throws IOException;

    /**
     * post请求
     * @param url url
     * @param payload String请求体，将保持原样
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    String post(String url, String payload, Header... header) throws IOException;

    /**
     * post请求
     * @param url url
     * @param payload String请求体，将保持原样
     * @param retry true重试， false不重试
     * @param header 请求头
     * @return 响应字符串
     * @throws IOException
     */
    String post(String url, String payload, boolean retry, Header... header) throws IOException;
}
