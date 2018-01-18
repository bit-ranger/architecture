package com.rainyalley.architecture.core.arithmetic.sort;

import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.IOUtils.EOF;


/**
 * @author bin.zhang
 */
@NotThreadSafe
public class FileStore<T extends Comparable<T>> implements Store<T> {

    private RandomAccessFile raf;

    private String filePath;

    private long size = 0;

    private int bufferSize;

    private ByteBuffer byteBuffer;

    private ByteBuffer unitBuffer;

    private ByteDataConverter<T> bdc;

    private FileStore<T> fork;

    private FileStore(){}

    public FileStore(String filePath, long size, ByteDataConverter<T> bdc){
        this(filePath, size, bdc, 1000);
    }

    /**
     *
     * @param filePath
     * @param size
     * @param bdc
     * @param bufferNum 对多少条数据进行缓冲
     */
    public FileStore(String filePath, long size, ByteDataConverter<T> bdc, int bufferNum){
        this.filePath = filePath;
        this.size = size;
        this.bdc = bdc;
        //bufferSize 必须是整数倍
        this.bufferSize = (bdc.unitBytes() + bdc.unitSeparator().length) * bufferNum;
    }


    @Override
    public String name() {
        return filePath;
    }

    @Override
    public FileStore<T> fork(String name, long size) {

        if(fork == null){
            fork = new FileStore<T>(name, size , bdc);
        } else {
            fork.filePath = name;
            fork.size = size;
            fork.raf = null;
        }

        return fork;
    }


    private long seekToIndex(long index)  throws IOException {
        initRaf();
        initBuffer();
        long seek = index == 0 ? 0 : index * (bdc.unitBytes() + bdc.unitSeparator().length);
        raf.seek(seek);
        return seek;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(raf);
    }

    @Override
    public boolean delete() {
        if(raf == null){
            return true;
        }
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

        try {
            seekToIndex(index);
            unitBuffer.clear();
            byte[] buffer = unitBuffer.array();
            read(raf, buffer, 0, bdc.unitBytes());
            return bdc.toData(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            unitBuffer.clear();
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


        try {
            seekToIndex(index);
            int num = Long.valueOf(length).intValue();
            int wws = bdc.unitBytes() + bdc.unitSeparator().length;
            byteBuffer.clear();
            byte[] buffer = byteBuffer.array();
            //~~todo~~ 不创建新的ArrayList
            List<T> dataList = new ArrayList<>(num);

            //总字节
            int tn = wws * num;
            //已读取的字节数
            int rn = 0;
            //本次已读取的字节
            int n = 0;

            loop:
            while ((n = read(raf, buffer, 0, bufferSize)) > 0){
                ByteBuffer subBuffer = unitBuffer;
                for (int i = 0; i < n; i+=wws) {
                    subBuffer.clear();
                    subBuffer.put(buffer, i, bdc.unitBytes());
                    T t = bdc.toData(subBuffer.array());
                    dataList.add(t);

                    rn += wws;
                    if(rn == tn){
                        break loop;
                    }

                    if(rn > tn){
                        throw new IllegalArgumentException(String.format("rn[%s] > tn[%s]", rn, tn));
                    }
                }


            }

            return dataList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            byteBuffer.clear();
            unitBuffer.clear();
        }
    }

    @Override
    public void set(long index, T data) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        try {
            seekToIndex(index);
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


        try {
            seekToIndex(index);
            int wws = bdc.unitBytes() + bdc.unitSeparator().length;
            int tn = wws * dataList.size();

            byteBuffer.clear();
            ByteBuffer buffer = byteBuffer;

            int n = 0;
            for (T t : dataList) {
                byte[] data = bdc.toByteArray(t);
                buffer.put(data);
                n+= data.length;
                if(bdc.unitSeparator().length > 0){
                    buffer.put(bdc.unitSeparator());
                    n+=bdc.unitSeparator().length;
                }

                //满了
                if(n % bufferSize == 0){
                    raf.write(buffer.array());
                    buffer.clear();
                }

                //光了
                if(n == tn){
                    break;
                }

                if(n > tn){
                    throw new IllegalArgumentException(String.format("n[%s] > tn[%s]", n, tn));
                }
            }

            if(n % bufferSize > 0){
                raf.write(buffer.array(), 0, n % bufferSize);
                buffer.clear();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            byteBuffer.clear();
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

    private void initRaf(){
        if(raf == null){
            try {
                this.raf = new RandomAccessFile(new File(filePath), "rw");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initBuffer(){
        if(byteBuffer == null && unitBuffer == null){
            byteBuffer  = ByteBuffer.allocate(bufferSize);
            unitBuffer = ByteBuffer.allocate(bdc.unitBytes());
        }
    }
}
