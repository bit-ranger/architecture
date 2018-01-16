package com.rainyalley.architecture.core.arithmetic.sort;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.IOUtils.EOF;


/**
 * @author bin.zhang
 */
public class FileStore<T extends Comparable<T>> implements Store<T> {

    private RandomAccessFile raf;

    private String filePath;

    private long size = 0;

    private ByteDataConverter<T> bdc;


    public FileStore(String filePath, long size, ByteDataConverter<T> bdc){
        try {
            this.filePath = filePath;
            this.size = size;
            this.bdc = bdc;
            this.raf = new RandomAccessFile(new File(filePath), "rw");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return filePath;
    }

    @Override
    public FileStore<T> fork(String name, long size) {
        return new FileStore<T>(name, size, bdc);
    }


    private long seekToIndex(long index){
        if(index == 0){
            return 0;
        }
        return index * (bdc.unitBytes() + bdc.unitSeparator().length);
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(raf);
    }

    @Override
    public boolean delete() {
        close();
        File file = new File(filePath);
        if(!file.exists()){
            return true;
        }
        return file.delete();
    }


    @Override
    public T get(long index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        long seek = seekToIndex(index);

        try {
            raf.seek(seek);
            byte[] buffer = ByteBuffer.allocate(bdc.unitBytes()).array();
            read(raf, buffer, 0, bdc.unitBytes());
            return bdc.toData(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int read(final RandomAccessFile input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.read(buffer, offset + location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }

    @Override
    public List<T> get(long index, long length) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        int num = Long.valueOf(length).intValue();
        long seek = seekToIndex(index);
        try {
            int wws = bdc.unitBytes() + bdc.unitSeparator().length;
            byte[] buffer = ByteBuffer.allocate(wws * num).array();
            raf.seek(seek);
            read(raf, buffer, 0, buffer.length);
            List<T> dataList = new ArrayList<>(num);
            ByteBuffer subBuffer = ByteBuffer.allocate(bdc.unitBytes());
            for (int i = 0; i < buffer.length; i+=wws) {
                subBuffer.clear();
                subBuffer.put(buffer, i, bdc.unitBytes());
                T t = bdc.toData(subBuffer.array());
                dataList.add(t);
            }
            return dataList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(long index, T data) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }
        long seek = seekToIndex(index);

        try {
            raf.seek(seek);
            raf.write(bdc.toByteArray(data));
            if(bdc.unitSeparator().length > 0){
                raf.write(bdc.unitSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(long index, List<T> dataList) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        long seek = seekToIndex(index);
        int wws = bdc.unitBytes() + bdc.unitSeparator().length;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(wws * dataList.size());
            for (T t : dataList) {
                byte[] data = bdc.toByteArray(t);
                buffer.put(data);
                if(bdc.unitSeparator().length > 0){
                    buffer.put(bdc.unitSeparator());
                }
            }
            raf.seek(seek);
            raf.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyFrom(long descIndex, Store<T> src, long srcIndex, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long size() {
        return size;
    }
}
