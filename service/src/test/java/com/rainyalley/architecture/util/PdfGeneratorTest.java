package com.rainyalley.architecture.util;

import org.junit.Test;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PdfGeneratorTest {

    private PdfGenerator generator = new PdfGenerator("/ftl");

    @Test
    public void resolve() throws Exception {
        Map<String, Long> data = new HashMap<>();
        data.put("total", 99995656L);
        String content = generator.resolve("testPdfGenerator.ftl", data);
        System.out.println(content);
    }

    @Test
    public void write() throws Exception {
        Map<String, Long> data = new HashMap<>();
        data.put("total", 99995656L);
        String content = generator.resolve("testPdfGenerator.ftl", data);
        generator.write(content, new FileOutputStream("/var/testPdfGenerator.pdf"));
    }

}