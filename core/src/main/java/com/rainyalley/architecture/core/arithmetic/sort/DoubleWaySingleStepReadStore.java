package com.rainyalley.architecture.core.arithmetic.sort;

import java.io.IOException;
import java.util.List;

public class DoubleWaySingleStepReadStore<T extends Comparable<T>> implements Store<T>  {

    private Store<T> store;

    private CachedStore<T> left;

    private CachedStore<T> right;

    private long leftPrevIndex = -1;

    private long rightPrevIndex = -1;

    public DoubleWaySingleStepReadStore(Store<T> store) {
        this.store = store;
        this.left = new CachedStore<>(store, 1000, 0, store.size() -1);
        this.right = new CachedStore<>(store, 1000, 0, store.size() -1);
    }

    @Override
    public String name() {
        return store.name();
    }

    @Override
    public Store<T> fork(String name, long size) {
        return new CachedStore<>(store.fork(name, size), 1000, 0, store.size() -1);
    }

    @Override
    public boolean delete() {
        return left.delete() & right.delete();
    }

    @Override
    public T get(long index) {
        if(index >= store.size() || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + store.size());
        }

        if(index == 0) {
            leftPrevIndex = index;
            return left.get(index);
        } else if(rightPrevIndex == -1){
            rightPrevIndex = index;
            return right.get(index);
        } else if(index == leftPrevIndex){
            leftPrevIndex = index;
            return left.get(index);
        } else if(index == rightPrevIndex){
            rightPrevIndex = index;
            return right.get(index);
        } else if(index == leftPrevIndex + 1){
            leftPrevIndex = index;
            return left.get(index);
        }  else if(index == rightPrevIndex + 1){
            rightPrevIndex = index;
            return right.get(index);
        }  else {
            throw new UnsupportedOperationException(String.format("index[%s] must inc by 1, leftPrevIndex[%s], rightPrevIndex[%s]",index, leftPrevIndex, rightPrevIndex));
        }
    }

    @Override
    public List<T> get(long index, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(long index, T data) {
        left.set(index, data);
        right.set(index, data);
    }

    @Override
    public void set(long index, List<T> dataList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copyFrom(long descIndex, Store<T> src, long srcIndex, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long size() {
        return store.size();
    }

    @Override
    public void close() throws IOException {
        left.close();
        right.close();
    }
}
