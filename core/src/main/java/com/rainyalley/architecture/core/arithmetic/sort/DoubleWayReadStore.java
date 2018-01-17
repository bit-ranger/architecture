package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.IOException;

public class DoubleWayReadStore<T extends Comparable<T>> {

    private Store<T> store;

    private CachedStore<T> left;

    private CachedStore<T> right;

    private int maxCacheSize;


    public DoubleWayReadStore(Store<T> store, int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        this.store = store;
        this.left = new CachedStore<>(store, maxCacheSize, 0, store.size() -1);
        this.right = new CachedStore<>(store, maxCacheSize, 0, store.size() -1);
    }

    public String name() {
        return store.name();
    }

    public Store<T> fork(String name, long size) {
        Store<T> fork = store.fork(name, size);
        return new CachedStore<>(fork, maxCacheSize, 0, fork.size() -1, false);
    }

    public boolean delete() {
        return left.delete() & right.delete();
    }

    public T getLeftWay(long index) {
        if(index >= store.size() || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + store.size());
        }

        return left.get(index);
    }

    public T getRightWay(long index) {
        if(index >= store.size() || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + store.size());
        }

        return right.get(index);
    }

    public void set(long index, T data) {
        left.set(index, data);
        right.set(index, data);
    }


    public long size() {
        return store.size();
    }

    public void close() throws IOException {
        left.close();
        right.close();
    }

    public void flush(){
        left.flush();
        right.flush();
    }
}
