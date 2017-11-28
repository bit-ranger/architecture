package com.rainyalley.architecture.core.arithmetic.sort;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FileExternalStoreTest {

    int numbers = 50;

    FileExternalStore<Integer> integerFileExternalStore = new FileExternalStore<>("/var/sort/FileExternalStoreTest", numbers, new IntegerByteDataConverter());

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
        integerFileExternalStore.set(0, integers);
        List<Integer> dataList = integerFileExternalStore.get(0,50);
        Assert.assertEquals(dataList, integers);
    }

    @Test
    public void set() throws Exception {
        integerFileExternalStore.set(0, integers);
    }

    @Test
    public void set1() throws Exception {
    }

    @Test
    public void copyFrom() throws Exception {
    }

    @Test
    public void size() throws Exception {
    }

}