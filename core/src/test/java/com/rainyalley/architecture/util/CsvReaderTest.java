package com.rainyalley.architecture.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderTest {

    private static Charset charset = Charset.forName("UTF-8");

    private CsvReader reader;

    private BufferedWriter writer;


    @Before
    public  void before() throws Exception{
        File file = new File("/var/csvReader/architecture_user.csv");
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }

        writer = new BufferedWriter(new FileWriter(file));

        for (int i = 0; i < 8; i++) {
            writer.write("1");
            writer.newLine();
        }
        writer.flush();

        reader = new CsvReader(Channels.newInputStream(Files.newByteChannel(Paths.get("/var/csvReader/architecture_user.csv"), StandardOpenOption.READ)), charset);
    }

    @After
    public  void after() throws IOException {
        reader.close();
        writer.close();
        new File("/var/csvReader/architecture_user.csv").delete();
    }

    @Test
    public void readRow() throws Exception {
        List<List<String>> rows = new ArrayList<>();
        List<String> row;
        while ((row = reader.readRow()) != null){
            rows.add(row);
        }

        Assert.assertEquals(rows.size(), 8);
    }

    @Test
    public void rows() throws Exception {
        Assert.assertEquals(8, reader.rows().mapToLong(p ->1L).sum());
    }

}