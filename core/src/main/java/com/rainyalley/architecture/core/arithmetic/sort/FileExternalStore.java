package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            file.deleteOnExit();
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
    public List<T> get(long index, long length) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        byte[] dataBytes = ByteBuffer.allocate(byteData.unitBytes()*Long.valueOf(length).intValue()).array();
        int readBytes = 0;
        try {
            RandomAccessFile raf = ensureRaf();
            raf.seek(index * byteData.unitBytes());
            readBytes = raf.read(dataBytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        if(readBytes % byteData.unitBytes() != 0){
            throw new IllegalArgumentException();
        }

        ArrayList<T> dataList = new ArrayList<>(Long.valueOf(length).intValue());

        for (int i = 0; i < readBytes; i+=byteData.unitBytes()) {
            byte[] bytes = ByteBuffer.allocate(byteData.unitBytes()).array();
            System.arraycopy(dataBytes, i, bytes, 0, byteData.unitBytes());
            T data = byteData.toData(bytes);
            dataList.add(data);
        }

        dataList.trimToSize();
        return dataList;
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

    @Override
    public void set(long index, List<T> dataList) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        if(index + dataList.size() > size){
            throw new IndexOutOfBoundsException("index:" + (index + dataList.size()) + " size:" + size);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(dataList.size() * byteData.unitBytes());

        for (T data : dataList) {
            byte[] bytes = byteData.toByteArray(data);
            try {
                baos.write(bytes);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        RandomAccessFile raf = ensureRaf();
        try {
            raf.seek(index * byteData.unitBytes());
            raf.write(baos.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void copyFrom(long descIndex, ExternalStore<T> src, long srcIndex, long length) {
        if(descIndex >= size || descIndex < 0){
            throw new IndexOutOfBoundsException("index:" + descIndex + " size:" + size);
        }

        if(descIndex + length > size){
            throw new IndexOutOfBoundsException("index:" + descIndex + length + " size:" + size);
        }

        List<T> dataList = src.get(srcIndex, length);
        set(descIndex, dataList);
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
