package com.rainyalley.architecture.core.util;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SpeedLimitInputStreamTest {

    private static InputStream inputStream = null;

    private static InputStream limitIn = null;


    @BeforeClass
    public static void before() throws IOException, URISyntaxException{
        inputStream = Channels.newInputStream(Files.newByteChannel(Paths.get("/var/sort/architecture_user.csv"), StandardOpenOption.READ));
        limitIn = new SpeedLimitInputStream(inputStream, 62);
    }

    @Test
    public void read() throws Exception {
        byte[] buffer = new byte[62500];
        long b = System.currentTimeMillis();
        int n = limitIn.read(buffer);
        long a = System.currentTimeMillis();
        System.out.println(String.format("total: %s bit, second: %s s, speed: %s bit/s", n * 8, (a - b) / 1000, n * 8 / (a - b) * 1000));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(buffer, 0, n);
        System.out.println(baos.toString("UTF-8"));
    }

    @Test
    public void read1() throws Exception {
    }

}