package com.rainyalley.architecture.arithmetic.sort;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class IoSorterTest {



    @Test
    public void readLine() throws Exception{
        IoSorter ioSorter = new IoSorter();
        byte[] source = "lllll\r\nggggggg\r\n".getBytes();
        ByteBuffer line = ioSorter.readLine(new ByteArrayInputStream(source), ByteBuffer.allocate(2), new byte[2]);
        Assert.assertArrayEquals(line.array(), "lllll\r\n".getBytes());
    }
}