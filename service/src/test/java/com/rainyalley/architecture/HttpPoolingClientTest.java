package com.rainyalley.architecture;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootApplication
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

        Header header  = new BasicHeader(HttpHeaders.CONTENT_TYPE, "132");
        client.post("http://baidu.com", Collections.emptyMap(),header);
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