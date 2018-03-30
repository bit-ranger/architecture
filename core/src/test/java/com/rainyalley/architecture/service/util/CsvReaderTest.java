package com.rainyalley.architecture.service.util;

import org.junit.*;

import java.io.IOException;
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

    @Before
    public  void before() throws Exception{
        reader = new CsvReader(Channels.newInputStream(Files.newByteChannel(Paths.get(CsvReaderTest.class.getResource("/architecture_user.csv").toURI()), StandardOpenOption.READ)), charset);
    }

    @After
    public  void after() throws IOException {
        reader.close();
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
        Assert.assertEquals(reader.rows().mapToLong(p ->1L).sum(), 8);
    }

}