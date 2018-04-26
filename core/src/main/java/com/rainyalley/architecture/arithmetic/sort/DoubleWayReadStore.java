package com.rainyalley.architecture.arithmetic.sort;

import net.jcip.annotations.NotThreadSafe;

import java.io.IOException;

/**
 * @author bin.zhang
 */
@NotThreadSafe
public class DoubleWayReadStore<T extends Comparable<T>> {

    private Store<T> store;

    private CachedStore<T> left;

    private CachedStore<T> right;

    private CachedStore<T> fork;



    public DoubleWayReadStore(Store<T> store, int maxCacheSize) {
        this.store = store;
        this.left = new CachedStore<>(store, maxCacheSize, 0, store.size() -1);
        this.right = new CachedStore<>(store, maxCacheSize, 0, store.size() -1);
        this.fork = new CachedStore<>(store, maxCacheSize, 0, store.size() - 1);
    }

    public String name() {
        return store.name();
    }

    public Store<T> fork(String name, long size) {
        return fork.fork(name, size);
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
    }
}
