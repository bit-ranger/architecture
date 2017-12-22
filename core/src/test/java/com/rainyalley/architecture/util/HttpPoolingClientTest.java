package com.rainyalley.architecture.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpPoolingClientTest{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private HttpPoolingClient client = new HttpPoolingClient();

    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void init() throws Exception {
        client.init();
    }

    @Test
    public void doExecute() throws Exception {
    }

    @Test
    public void get() throws Exception {
        client.get("http://baidu.com");
    }

    @Test
    public void post() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        map.put("aaa", "bbbb");
       // Header header  = new BasicHeader(HttpHeaders.CONTENT_TYPE, "132");
        client.post("http://baidu.com", map, true);
    }

    @Test
    public void postJon() throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        map.put("aaa", "bbbb");
        //Header header  = new BasicHeader(HttpHeaders.CONTENT_TYPE, "132");
        client.post("http://baidu.com", objectMapper.writeValueAsString(map), true);
    }

    @Test
    public void close() throws Exception {
    }

    @Test
    public void getParams() throws Exception {
    }

    @Test
    public void getConnectionManager() throws Exception {
    }

}