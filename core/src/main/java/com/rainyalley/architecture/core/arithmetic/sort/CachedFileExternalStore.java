package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * 不要倒序访问
 * @param <T>
 */
public class CachedFileExternalStore<T extends Comparable<T>> implements ExternalStore<T> {

    private File file;

    private RandomAccessFile raf;

    private long size;

    private ByteData<T> byteData;

    private long bufferStart = 0;

    private int bufferSize = 100;

    private long bufferEnd = bufferSize - 1 ;

    private Object[] bufferValue;


    public CachedFileExternalStore(String fileName, long size, ByteData<T> byteData){
        this(fileName, size, byteData, 100);
    }

    public CachedFileExternalStore(String fileName, long size, ByteData<T> byteData, int bufferSize){
        try {
            this.file = new File(fileName);
            this.raf = new RandomAccessFile(file, "rw");
            this.size = size;
            this.byteData = byteData;
            this.bufferSize = bufferSize;
            this.bufferEnd =  Math.min(bufferSize, size) - 1;
            this.bufferValue = new Object[bufferSize];
            fillBuffer();
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
        return new CachedFileExternalStore<>(name, size, byteData, bufferSize);
    }

    @Override
    public void close() {
        flushBuffer();

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

        moveBuffer(index);

        int iib = indexInBufferValue(index);

        return (T)bufferValue[iib];
    }

    @Override
    public void set(long index, T data) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        moveBuffer(index);

        int iib = indexInBufferValue(Long.valueOf(index).intValue());

        bufferValue[iib] = data;
    }

    @Override
    public long size() {
        return size;
    }


    private void moveBuffer(long index){


        if(index > bufferEnd){
            System.out.println(String.format("will moveBufferRight %s,%s,%s", bufferStart, bufferEnd, index));
            moveBufferRight(index);
        }

        if(index < bufferStart){
            System.out.println(String.format("will moveBufferLeft %s,%s,%s", bufferStart, bufferEnd, index));
            moveBufferLeft(index);
        }
    }

    private void moveBufferRight(long index){
        flushBuffer();
        bufferStart = index;
        bufferEnd = Math.min(bufferStart + bufferSize - 1, size-1);
        fillBuffer();
    }

    private void moveBufferLeft(long index){
        flushBuffer();
        bufferStart = index;
        bufferEnd = Math.min(bufferStart + bufferSize - 1, size-1);
        fillBuffer();
    }

    private void fillBuffer(){
        try {
            raf.seek(bufferStart * byteData.unitBytes());
            byte[] bufferDataBytes = ByteBuffer.allocate(byteData.unitBytes() * Long.valueOf(bufferEnd - bufferStart + 1).intValue()).array();
            raf.read(bufferDataBytes);
            for (long i = bufferStart; i <= bufferEnd; i++) {
                try {
                    int iib = indexInBufferValue(i);
                    byte[] dataBytes = ByteBuffer.allocate(byteData.unitBytes()).array();
                    System.arraycopy(bufferDataBytes, iib * byteData.unitBytes(), dataBytes, 0, byteData.unitBytes());
                    T data = byteData.toData(dataBytes);
                    bufferValue[iib] = data;
                } catch (Exception e) {
                    throw new IllegalArgumentException(String.format("bufferStart[%s],bufferEnd[%s]", bufferStart, bufferEnd),e);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void flushBuffer(){
        try {
            raf.seek(bufferStart * byteData.unitBytes());
            ByteArrayOutputStream bos = new ByteArrayOutputStream(Long.valueOf(bufferEnd - bufferStart + 1).intValue() * byteData.unitBytes());
            for (long i = bufferStart; i <= bufferEnd; i++) {
                try {
                    int iib = indexInBufferValue(i);
                    byte[] dataBytes = byteData.toByteArray((T)bufferValue[iib]);
                    bos.write(dataBytes);
                } catch (Exception e) {
                    throw new IllegalArgumentException(String.format("bufferStart[%s],bufferEnd[%s]", bufferStart, bufferEnd),e);
                }
            }
            raf.write(bos.toByteArray());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private int indexInBufferValue(long index){
        Long bt = index - bufferStart;
        return bt.intValue();
    }


}
