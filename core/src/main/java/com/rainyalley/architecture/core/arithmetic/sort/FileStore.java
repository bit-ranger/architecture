package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.List;

import static org.apache.commons.io.IOUtils.EOF;


/**
 * @author bin.zhang
 */
public class FileStore<T extends Comparable<T>> implements ExternalStore<T> {

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
    public ExternalStore<T> create(String name, long size) {
        return new FileStore<T>(name, size, bdc);
    }


    private long seekToIndex(long index){
        if(index == 0){
            return 0;
        }
        return index * (bdc.unitBytes() + bdc.unitSeparator().length) - 1;
    }

    @Override
    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void copyFrom(long descIndex, ExternalStore<T> src, long srcIndex, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long size() {
        return size;
    }
}
