package com.rainyalley.architecture.core.arithmetic.sort;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentFileStoreTest {

    private static int numbers = 10000;

    private static ConcurrentFileStore<Integer> is = new ConcurrentFileStore<>("/var/sort/ConcurrentFileStoreTest", numbers, 10000, new IntegerByteDataConverter());
    private static ConcurrentFileStore<Integer> is2 = new ConcurrentFileStore<>("/var/sort/ConcurrentFileStoreTest2", numbers, 10000,new IntegerByteDataConverter());

    private List<Integer> integers = new ArrayList<>();



    @Before
    public void before(){
        integers.clear();
        for (int i = 0; i < numbers; i++) {
            integers.add(Double.valueOf(Math.random()*numbers).intValue());
        }
    }

    @AfterClass
    public static void afterClass(){
        is2.close();
        is.close();
    }


    @Test
    public void get() throws Exception {
        is.set(0, integers);
        List<Integer> dataList = is.get(0,numbers);
        Assert.assertEquals(dataList, integers);
    }

    @Test
    public void set() throws Exception {
        is.set(0, integers);
    }


    @Test
    public void copyFrom() throws Exception {
        is.set(0, integers);
        is2.copyFrom(0, is, 0, numbers);
        for (int i = 0; i < numbers; i++) {
            Assert.assertEquals(is.get(i), is2.get(i));
        }
    }

}