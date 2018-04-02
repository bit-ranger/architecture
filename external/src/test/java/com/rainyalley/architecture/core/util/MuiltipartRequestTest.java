package com.rainyalley.architecture.core.util;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileInputStream;

public class MuiltipartRequestTest {

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public void post() throws Exception{
        HttpPost uploadFile = new HttpPost("http://localhost:8080");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addTextBody("Version", "30", ContentType.TEXT_PLAIN);
        builder.addTextBody("CmdId", "BatchTrans", ContentType.TEXT_PLAIN);
        builder.addTextBody("merCustId", "789456", ContentType.TEXT_PLAIN);
        builder.addTextBody("batchId", "123456", ContentType.TEXT_PLAIN);
        builder.addTextBody("merOrdDate", "20180102", ContentType.TEXT_PLAIN);
        builder.addTextBody("transType", "1", ContentType.TEXT_PLAIN);
        builder.addTextBody("bgRetUrl", "http://www.baidu.com", ContentType.TEXT_PLAIN);
        File f = new File(MuiltipartRequestTest.class.getResource("/architecture_user.csv").getPath());
        builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();
        responseEntity.writeTo(System.out);
    }

}
