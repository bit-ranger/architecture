package com.rainyalley.architecture.arithmetic.sort;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class ConcurrentFileStore<T extends Comparable<T>> implements Store<T> {

    private  final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Path path;

    private long size;

    private ByteDataConverter<T> byteData;

    private GenericObjectPool<SeekableByteChannel> pool;


    private long copyNumber;


    public ConcurrentFileStore(String fileName, long size, ByteDataConverter<T> byteData){
        this(fileName, size, 500, byteData);
    }



    public ConcurrentFileStore(String fileName, long size, long copyNumber, ByteDataConverter<T> byteData){
        try {
            this.path = Paths.get(fileName);
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(3200);
            pool = new GenericObjectPool<>(new SeekableFatory(path), config);
            this.size = size;
            this.byteData = byteData;
            this.copyNumber = copyNumber;

            LOGGER.debug("create {}", fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return path.toString();
    }

    @Override
    public Store<T> fork(String name, long size) {
        return new ConcurrentFileStore<>(name, size, copyNumber, byteData);
    }

    @Override
    public  void  close() {
        File file = path.toFile();
        if(!file.delete()){
            file.deleteOnExit();
        }

        LOGGER.debug("close {}", name());
    }

    @Override
    public boolean delete() {
        close();
        return true;
    }


    @Override
    public T get(long index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        ByteBuffer dataBytes = ByteBuffer.allocate(byteData.unitBytes());

        SeekableByteChannel sbc = null;
        try{
            sbc = pool.borrowObject();
            sbc.position(index * byteData.unitBytes());
            sbc.read(dataBytes);
            return byteData.toData(dataBytes.array());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if(sbc!=null){
                pool.returnObject(sbc);
            }
        }

    }

    @Override
    public List<T> get(long index, long length) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        ByteBuffer dataBytes = ByteBuffer.allocate(byteData.unitBytes()*Long.valueOf(length).intValue());
        int readBytes = 0;
        SeekableByteChannel sbc = null;
        try {
            sbc = pool.borrowObject();
            sbc.position(index * byteData.unitBytes());
            readBytes = sbc.read(dataBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if(sbc != null){
                pool.returnObject(sbc);
            }
        }

        if(readBytes % byteData.unitBytes() != 0){
            throw new IllegalArgumentException();
        }

        ArrayList<T> dataList = new ArrayList<>(Long.valueOf(length).intValue());

        for (int i = 0; i < readBytes; i+=byteData.unitBytes()) {
            byte[] bytes = ByteBuffer.allocate(byteData.unitBytes()).array();
            System.arraycopy(dataBytes.array(), i, bytes, 0, byteData.unitBytes());
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
        SeekableByteChannel sbc = null;
        try {
            sbc = pool.borrowObject();
            sbc.position(index * byteData.unitBytes());
            sbc.write(ByteBuffer.wrap(dataBytes));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if(sbc != null){
                pool.returnObject(sbc);
            }
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

        SeekableByteChannel sbc = null;
        try {
            sbc = pool.borrowObject();
            sbc.position(index * byteData.unitBytes());
            sbc.write(ByteBuffer.wrap(baos.toByteArray()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if(sbc != null){
                pool.returnObject(sbc);
            }
        }

    }

    @Override
    public void copyFrom(long descIndex, Store<T> src, long srcIndex, long length) {
        if(descIndex >= size || descIndex < 0){
            throw new IndexOutOfBoundsException("index:" + descIndex + " size:" + size);
        }

        if(descIndex + length > size){
            throw new IndexOutOfBoundsException("index:" + descIndex + length + " size:" + size);
        }

        long copiedLength = 0;
        for (long i = 0; copyNumber < length && i < length; i+=copyNumber) {
            List<T> dataList = src.get(srcIndex + i, Math.min(copyNumber,length-copiedLength));
            set(descIndex + i, dataList);
            copiedLength += copyNumber;
        }

        if(copiedLength < length){
            List<T> dataList = src.get(srcIndex + copiedLength, length - copiedLength);
            set(descIndex + copiedLength, dataList);
        }

    }

    @Override
    public long size() {
        return size;
    }


    private static class SeekableFatory implements PooledObjectFactory<SeekableByteChannel> {

        private Path path;

        public SeekableFatory(Path path) {
            this.path = path;
        }

        @Override
        public PooledObject<SeekableByteChannel> makeObject() throws Exception {
            try {
                DefaultPooledObject<SeekableByteChannel> obj =  new DefaultPooledObject<>(Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)));
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void destroyObject(PooledObject<SeekableByteChannel> p) throws Exception {
            p.getObject().close();
        }

        @Override
        public boolean validateObject(PooledObject<SeekableByteChannel> p) {
            return p.getObject().isOpen();
        }

        @Override
        public void activateObject(PooledObject<SeekableByteChannel> p) throws Exception {

        }

        @Override
        public void passivateObject(PooledObject<SeekableByteChannel> p) throws Exception {

        }
    }
}
