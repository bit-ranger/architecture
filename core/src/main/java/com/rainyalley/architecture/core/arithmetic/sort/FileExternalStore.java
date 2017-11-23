package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * 不要倒序访问
 * @param <T>
 */
public class FileExternalStore<T extends Comparable<T>> implements ExternalStore<T> {

    private File file;

    private RandomAccessFile raf;

    private long size;

    private ByteData<T> byteData;



    public FileExternalStore(String fileName, long size, ByteData<T> byteData){
        try {
            this.file = new File(fileName);
            this.raf = new RandomAccessFile(file, "rw");
            this.size = size;
            this.byteData = byteData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return file.getPath();
    }

    @Override
    public ExternalStore<T> create(String name, long size) {
        return new FileExternalStore<>(name, size, byteData);
    }

    @Override
    public void close() {
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
            throw new IllegalStateException("deleted " + file.getName() + " failed");
        }
    }

    @Override
    public T get(long index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }


        byte[] dataBytes = ByteBuffer.allocate(byteData.unitBytes()).array();
        try {
            raf.seek(index * byteData.unitBytes());
            raf.read(dataBytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        T data = byteData.toData(dataBytes);
        return data;
    }

    @Override
    public void set(long index, T data) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        byte[] dataBytes = byteData.toByteArray(data);
        try {
            raf.seek(index * byteData.unitBytes());
            raf.write(dataBytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public long size() {
        return size;
    }


}
