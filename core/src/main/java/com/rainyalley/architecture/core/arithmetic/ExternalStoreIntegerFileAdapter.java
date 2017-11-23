package com.rainyalley.architecture.core.arithmetic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

class ExternalStoreIntegerFileAdapter implements ExternalMergeSort.ExternalStore<Integer> {

    private File file;

    private RandomAccessFile raf;

    /**
     * 每个单元4字节
     */
    private int unitBytes = 4;



    public ExternalStoreIntegerFileAdapter(String fileName, long size){
        try {
            this.file = new File(fileName);
            this.raf = new RandomAccessFile(file, "rw");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return file.getPath();
    }

    @Override
    public ExternalMergeSort.ExternalStore<Integer> create(String name, long size) {
        return new ExternalStoreIntegerFileAdapter(name, size);
    }

    @Override
    public void delete() {
        try {
            raf.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        if(!file.exists()){
            return;
        }
        boolean deleted = file.delete();
        if(!deleted){
            throw new IllegalStateException("delete " + file.getName() + " failed");
        }
    }

    @Override
    public Integer get(long index) {
        try {
            raf.seek(index * unitBytes);
            byte[] dataBytes = ByteBuffer.allocate(unitBytes).array();
            raf.read(dataBytes);
            return ByteBuffer.wrap(dataBytes).asIntBuffer().get();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void set(long index, Integer data) {
        try {
            raf.seek(index * unitBytes);
            byte[] dataBytes = ByteBuffer.allocate(unitBytes).putInt(data).array();
            raf.write(dataBytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public long size() {
        try {
            return raf.length() / unitBytes;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
