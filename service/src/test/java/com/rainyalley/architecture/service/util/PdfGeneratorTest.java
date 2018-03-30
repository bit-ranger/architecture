package com.rainyalley.architecture.service.util;

import org.junit.Test;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PdfGeneratorTest {
    private static final int maxTask = 32;

    /**
     * 0-4个线程活动
     * 阻塞队列容量100
     * 最大空闲时间10秒
     */
    private final static ThreadPoolExecutor es = new ThreadPoolExecutor(0, 32, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(maxTask));


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

        CountDownLatch latch = new CountDownLatch(maxTask);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Long> data = new HashMap<>();
                    data.put("total", 99995656L);
                    String content = generator.resolve("testPdfGenerator.ftl", data);
                    generator.write(content, new FileOutputStream("/var/testPdfGenerator" + Math.random() + ".pdf"));
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                } finally {
                    latch.countDown();
                }
            }
        };

        for (int i = 0; i < maxTask; i++) {
            es.execute(runnable);
        }

        latch.await();

    }

}