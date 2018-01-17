package com.rainyalley.architecture.core.arithmetic.sort;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class CsvByteDataConverter implements ByteDataConverter<CsvRow> {



    private int unitBytes = 0;

    private Charset charset = Charset.forName("UTF-8");

    private byte[] unitSeparator = System.lineSeparator().getBytes(charset);

    public CsvByteDataConverter(int unitBytes, Charset charset) {
        this.unitBytes = unitBytes;
        this.charset = charset;
    }

    private byte[] padding = " ".getBytes(charset);

    private byte[] paddingToWidth(byte[] lineBytes){
        if(lineBytes.length == this.unitBytes()){
            return lineBytes;
        } else if(lineBytes.length > this.unitBytes()){
            throw new IllegalArgumentException(String.format("lineBytes.length[%s] > this.unitBytes()[%s]", lineBytes.length, this.unitBytes));
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(this.unitBytes());
            if(lineBytes.length > 0){
                buffer.put(lineBytes);
            }
            for (int i = lineBytes.length; i < unitBytes; i++) {
                buffer.put(padding);
            }
            return buffer.array();
        }
    }

    @Override
    public byte[] toByteArray(CsvRow data) {
        byte[] bytes = null;
        if(data == null){
            bytes = new byte[0];
        } else {
            bytes = data.toString().getBytes(charset);
        }
        return paddingToWidth(bytes);
    }

    @Override
    public CsvRow toData(byte[] dataBytes) {
        if(dataBytes.length != this.unitBytes){
            throw new IllegalArgumentException(String.format("dataBytes.length must be %s", this.unitBytes));
        }
        return new CsvRow(new String(dataBytes, charset));
    }

    @Override
    public int unitBytes() {
        return unitBytes;
    }

    @Override
    public byte[] unitSeparator() {
        return unitSeparator;
    }

    public static Pair<Long, Integer> findSizeAndMaxLineWidth(BufferedReader br, Charset charset){
        long size = 0;
        int maxLineWidth = 0;
        try {
            String line = null;
            while ((line = br.readLine()) != null){
                size++;
                int len = line.getBytes(charset).length;
                if(len > maxLineWidth){
                    maxLineWidth = len;
                }
            }
            return new ImmutablePair<Long,Integer>(size, maxLineWidth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
