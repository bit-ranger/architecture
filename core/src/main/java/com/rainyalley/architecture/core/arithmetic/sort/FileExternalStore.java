package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * 不要倒序访问
 * @param <T>
 */
public class FileExternalStore<T extends Comparable<T>> implements ExternalStore<T> {

    private File file;

    private final Map<Thread, RandomAccessFile> rafMap = new HashMap<>();

    private long size;

    private ByteDataConverter<T> byteData;



    public FileExternalStore(String fileName, long size, ByteDataConverter<T> byteData){
        try {
            this.file = new File(fileName);
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
    public  void  close() {
        for (RandomAccessFile randomAccessFile : rafMap.values()) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        }
        rafMap.clear();
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
            RandomAccessFile raf = ensureRaf();
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
            RandomAccessFile raf = ensureRaf();
            raf.seek(index * byteData.unitBytes());
            raf.write(dataBytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RandomAccessFile ensureRaf(){
        RandomAccessFile raf = rafMap.get(Thread.currentThread());
        if(raf != null){
            return raf;
        }

        try {
            raf = new RandomAccessFile(file, "rw");
            synchronized (rafMap) {
                rafMap.put(Thread.currentThread(), raf);
            }
            return raf;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public long size() {
        return size;
    }


}
