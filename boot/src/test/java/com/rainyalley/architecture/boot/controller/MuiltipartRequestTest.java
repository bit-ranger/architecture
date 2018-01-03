package com.rainyalley.architecture.boot.controller;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MuiltipartRequestTest {

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    @Test
    public void post() throws Exception{
        HttpPost uploadFile = new HttpPost("http://localhost:8080/muser/reconciliation/upload");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addTextBody("Version", "30", ContentType.TEXT_PLAIN);
        builder.addTextBody("CmdId", "BatchTrans", ContentType.TEXT_PLAIN);
        builder.addTextBody("merCustId", "789456", ContentType.TEXT_PLAIN);
        builder.addTextBody("batchId", "123456", ContentType.TEXT_PLAIN);
        builder.addTextBody("merOrdDate", "20180102", ContentType.TEXT_PLAIN);
        builder.addTextBody("transType", "1", ContentType.TEXT_PLAIN);
        builder.addTextBody("bgRetUrl", "http://www.baidu.com", ContentType.TEXT_PLAIN);
        File f = new File("/Doc/存档/data/data.csv");
        builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();
        responseEntity.writeTo(System.out);
    }

}
