package com.rainyalley.architecture.core.arithmetic.sort;

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


public class FileExternalStore<T extends Comparable<T>> implements ExternalStore<T> {

    private Path path;

    private long size;

    private ByteDataConverter<T> byteData;


    private long copyNumber;


    public FileExternalStore(String fileName, long size, ByteDataConverter<T> byteData){
        this(fileName, size, 500, byteData);
    }



    public FileExternalStore(String fileName, long size, long copyNumber, ByteDataConverter<T> byteData){
        try {
            this.path = Paths.get(fileName);
            this.size = size;
            this.byteData = byteData;
            this.copyNumber = copyNumber;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return path.toString();
    }

    @Override
    public ExternalStore<T> create(String name, long size) {
        return new FileExternalStore<>(name, size, copyNumber, byteData);
    }

    @Override
    public  void  close() {
        File file = path.toFile();
        if(!file.delete()){
            file.deleteOnExit();
        }
    }


    @Override
    public T get(long index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        ByteBuffer dataBytes = ByteBuffer.allocate(byteData.unitBytes());
        try(SeekableByteChannel sbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ))) {
            sbc.position(index * byteData.unitBytes());
            sbc.read(dataBytes);
            return byteData.toData(dataBytes.array());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public List<T> get(long index, long length) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
        }

        ByteBuffer dataBytes = ByteBuffer.allocate(byteData.unitBytes()*Long.valueOf(length).intValue());
        int readBytes = 0;
        try (SeekableByteChannel sbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ))) {

            sbc.position(index * byteData.unitBytes());
            readBytes = sbc.read(dataBytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
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
        try(SeekableByteChannel sbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE))) {
            sbc.position(index * byteData.unitBytes());
            sbc.write(ByteBuffer.wrap(dataBytes));
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

        try (SeekableByteChannel sbc = Files.newByteChannel(path, EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE))){
            sbc.position(index * byteData.unitBytes());
            sbc.write(ByteBuffer.wrap(baos.toByteArray()));
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



}
