package com.rainyalley.architecture.util;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {HttpPoolingClientTest.class})
public class HttpPoolingClientTest{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private HttpPoolingClient client = new HttpPoolingClient();





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
        client.post("http://172.31.19.54:1234/muser/publicRequests/depoPublicRequests", JSONObject.toJSONString(map), true);
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