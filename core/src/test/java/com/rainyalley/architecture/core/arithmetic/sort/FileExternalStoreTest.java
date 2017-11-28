package com.rainyalley.architecture.core.arithmetic.sort;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FileExternalStoreTest {

    int numbers = 1000000;

    FileExternalStore<Integer> is = new FileExternalStore<>("/var/sort/FileExternalStoreTest", numbers, new IntegerByteDataConverter());
    FileExternalStore<Integer> is2 = new FileExternalStore<>("/var/sort/FileExternalStoreTest2", numbers, new IntegerByteDataConverter());

    List<Integer> integers = new ArrayList<>();



    @Before
    public void before(){
        for (int i = 0; i < numbers; i++) {
            integers.add(Double.valueOf(Math.random()*numbers).intValue());
        }
    }

    @Test
    public void name() throws Exception {

    }

    @Test
    public void create() throws Exception {
    }

    @Test
    public void close() throws Exception {
    }

    @Test
    public void get() throws Exception {
    }

    @Test
    public void get1() throws Exception {
        is.set(0, integers);
        List<Integer> dataList = is.get(0,numbers);
        Assert.assertEquals(dataList, integers);
    }

    @Test
    public void set() throws Exception {
        is.set(0, integers);
    }

    @Test
    public void set1() throws Exception {
    }

    @Test
    public void copyFrom() throws Exception {
        is.set(0, integers);
        is2.copyFrom(0, is, 0, numbers);
        for (int i = 0; i < numbers; i++) {
            Assert.assertEquals(is.get(i), is2.get(i));
        }
    }

    @Test
    public void size() throws Exception {
    }

}