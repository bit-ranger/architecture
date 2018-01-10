package com.rainyalley.architecture.core.arithmetic.sort;

import java.util.List;

public class LineNumberExternalStore implements ExternalStore<String> {

    @Override
    public String name() {
        return null;
    }

    @Override
    public ExternalStore<String> create(String name, long size) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public String get(long index) {
        return null;
    }

    @Override
    public List<String> get(long index, long length) {
        return null;
    }

    @Override
    public void set(long index, String data) {

    }

    @Override
    public void set(long index, List<String> dataList) {

    }

    @Override
    public void copyFrom(long descIndex, ExternalStore<String> src, long srcIndex, long length) {

    }

    @Override
    public long size() {
        return 0;
    }
}
