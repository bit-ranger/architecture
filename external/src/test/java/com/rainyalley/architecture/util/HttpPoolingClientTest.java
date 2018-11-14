package com.rainyalley.architecture.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpPoolingClientTest{

    private ExecutorService es =  Executors.newFixedThreadPool(2);

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
        CountDownLatch cl = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            final int fi = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    // Header header  = new BasicHeader(HttpHeaders.CONTENT_TYPE, "132");
                    try {

                        client.post("http://baidu.com", map, fi % 2 == 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        cl.countDown();
                    }
                }
            });
        }

        cl.await();

        System.out.println(client.getCookieStore().getCookies());
    }

    @Test
    public void postJon() throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        map.put("aaa", "bbbb");
        //Header header  = new BasicHeader(HttpHeaders.CONTENT_TYPE, "132");
        client.post("https://b2buat.liby.com.cn/libyb2bocc/v2/liby/checkToken?access_token=916e9c73-bf16-4d25-93e0-abc720cff866", "", false);
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