package com.rainyalley.architecture.arithmetic.sort;

import com.rainyalley.architecture.util.Assert;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author bin.zhang
 */
@NotThreadSafe
public class CachedStore<T extends Comparable<T>> implements Store<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedStore.class);

    private Store<T> store;

    private long currentCacheBeginIndex;

    private long cacheRangeBeginIndex;

    private long cacheRangeEndIndex;

    private Object[] cacheList;

    private int realCacheSize;

    private CachedStore<T> fork;

    private  boolean isClosed = false;

    public CachedStore(Store<T> store, int maxCacheSize, long cacheRangeBeginIndex, long cacheRangeEndIndex) {
        this(store, maxCacheSize, cacheRangeBeginIndex, cacheRangeEndIndex, true);
    }

    public CachedStore(Store<T> store, int maxCacheSize, long cacheRangeBeginIndex, long cacheRangeEndIndex, boolean load) {
        Assert.isTrue(cacheRangeBeginIndex >= 0);
        Assert.isTrue(cacheRangeEndIndex < store.size());

        this.store = store;
        this.cacheRangeBeginIndex = cacheRangeBeginIndex;
        this.cacheRangeEndIndex = cacheRangeEndIndex;

        this.currentCacheBeginIndex = cacheRangeBeginIndex;
        realCacheSize = Math.min(maxCacheSize, Long.valueOf(cacheRangeEndIndex - cacheRangeBeginIndex).intValue() + 1);
        realCacheSize = Math.min(realCacheSize, Long.valueOf(store.size()).intValue());
        this.cacheList = new Object[realCacheSize];

        if(load){
            List<T> dataList = store.get(currentCacheBeginIndex, realCacheSize);
            dataList.toArray(cacheList);
        }
    }

    private CachedStore(){}

    @Override
    public String name() {
        return store.name();
    }

    @Override
    public CachedStore<T> fork(String name, long size) {

        if(fork == null){
            fork = new CachedStore<>();
            fork.cacheList = new Object[this.realCacheSize];
            fork.cacheRangeBeginIndex = 0;
        }
        fork.store = store.fork(name, size);
        fork.currentCacheBeginIndex = 0;
        fork.cacheRangeEndIndex = size - 1;
        int realCacheSize = Math.min(fork.cacheList.length, Long.valueOf(cacheRangeEndIndex - cacheRangeBeginIndex).intValue() + 1);
        realCacheSize = Math.min(realCacheSize, Long.valueOf(size).intValue());
        fork.realCacheSize = realCacheSize;

        return fork;
    }

    @Override
    public void close() {
        if(isClosed){
            return;
        }

        IOUtils.closeQuietly(store);
        isClosed = true;
    }

    @Override
    public boolean delete() {
        close();
        return store.delete();
    }


    @Override
    public T get(long index) {
        if(index >= store.size() || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + store.size());
        }

        if(inCurrentCache(index)){
            return getFromCache(index);
        } if(outOfCacheRange(index)){
            return store.get(index);
        } else {
            moveCache(index);
            if(inCurrentCache(index)){
                return getFromCache(index);
            } else {
                throw new IllegalArgumentException(String.format("index not in cache %s", index));
            }
        }

    }

    private boolean inCurrentCache(long index){
        return index >= currentCacheBeginIndex && index < currentCacheBeginIndex + realCacheSize;
    }

    private boolean outOfCacheRange(long index){
        return index < cacheRangeBeginIndex || index > cacheRangeEndIndex;
    }

    private T getFromCache(long index){
        return (T) cacheList[Long.valueOf(index - currentCacheBeginIndex).intValue()];
    }

    private void setToCache(long index, T data){
        cacheList[Long.valueOf(index - currentCacheBeginIndex).intValue()] = data;
    }


    private void moveCache(long index){
        flush();
        long maxCacheBeginIndex = cacheRangeEndIndex - realCacheSize + 1;
        currentCacheBeginIndex = Math.min(maxCacheBeginIndex, index);
        List<T> dataList = store.get(currentCacheBeginIndex, realCacheSize);
        Assert.isTrue(dataList.size() <= realCacheSize, "dataList.size() > realCacheSize");
        dataList.toArray(cacheList);
//        if(LOGGER.isDebugEnabled()){
//            LOGGER.debug("moveCache@{} to {}, cause {}", hashCode() + ":" + name(), currentCacheBeginIndex, index);
//        }
    }

    @Override
    public List<T> get(long index, long length) {
        //~~todo~~ 不创建新的ArrayList
        ArrayList<T> al = new ArrayList<T>(Long.valueOf(length).intValue());
        for (int i = 0; i < length; i++) {
            T t = this.get(index + i);
            al.add(t);
        }
        return al;
    }

    @Override
    public void set(long index, T data) {
        if(index >= store.size() || index < 0){
            throw new IndexOutOfBoundsException("index:" + index + " size:" + store.size());
        }

        if(inCurrentCache(index)){
            setToCache(index, data);
        } else if(outOfCacheRange(index)){
            store.set(index, data);
        } else {
            moveCache(index);
            if(inCurrentCache(index)){
                setToCache(index, data);
            } else {
                throw new IllegalArgumentException(String.format("index not in cache %s", index));
            }
        }
    }

    @Override
    public void set(long index, List<T> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            this.set(index + i, dataList.get(i));
        }
    }

    @Override
    public void copyFrom(long descIndex, Store<T> src, long srcIndex, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long size() {
        return store.size();
    }

    public void flush(){
        //~~todo~~ 不创建新的ArrayList
        ArrayList<T> al = new ArrayList<>(realCacheSize);
        for (Object o : cacheList) {
            al.add((T)o);
        }
        store.set(currentCacheBeginIndex, al);
    }
}
