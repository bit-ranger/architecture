package com.rainyalley.architecture.core.arithmetic;

import java.io.File;

class ExternalStoreIntegerFileAdapter implements ExternalMergeSort.ExternalStore<Integer> {

    private File file;


    private Integer[] arr;



    public ExternalStoreIntegerFileAdapter(String fileName, long size){
        try {
            this.file = new File(fileName);
            arr = new Integer[(int)size];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return file.getName();
    }

    @Override
    public ExternalMergeSort.ExternalStore<Integer> create(String name, long size) {
        return new ExternalStoreIntegerFileAdapter(name, size);
    }

    @Override
    public void delete() {
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
        return arr[(int)index];
    }

    @Override
    public void set(long index, Integer data) {
        arr[(int)index] = data;
    }

    @Override
    public long size() {
        return arr.length;
    }
}
