package com.rainyalley.architecture.core.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SpeedLimitInputStreamTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedLimitInputStreamTest.class);


    private static InputStream inputStream = null;

    private static InputStream limitIn = null;


    @BeforeClass
    public static void before() throws IOException, URISyntaxException{
        File file = new File("/var/sort/architecture_user.csv");
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }

        BufferedWriter  writer = new BufferedWriter(new FileWriter(file));

        for (int i = 0; i < 8; i++) {
            writer.write("1");
            writer.newLine();
        }
        writer.flush();
        writer.close();

        inputStream = Channels.newInputStream(Files.newByteChannel(Paths.get("/var/sort/architecture_user.csv"), StandardOpenOption.READ));
        limitIn = new SpeedLimitInputStream(inputStream, 62);
    }

    @AfterClass
    public static void after(){
        File file = new File("/var/sort/architecture_user.csv");
        file.delete();
    }

    @Test
    public void read() throws Exception {
        byte[] buffer = new byte[62500];
        long b = System.currentTimeMillis();
        int n = limitIn.read(buffer);
        long a = System.currentTimeMillis();
        LOGGER.info(String.format("total: %s bit, second: %s s, speed: %s bit/s", n * 8, (a - b) / 1000, n * 8 / (a - b) * 1000));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(buffer, 0, n);
        LOGGER.info(baos.toString("UTF-8"));
    }

    @Test
    public void read1() throws Exception {
    }

}