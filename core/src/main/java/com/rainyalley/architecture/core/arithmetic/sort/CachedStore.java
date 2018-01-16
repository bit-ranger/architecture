package com.rainyalley.architecture.core.arithmetic.sort;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author bin.zhang
 */
public class CachedStore<T extends Comparable<T>> implements Store<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedStore.class);

    private Store<T> store;

    private int maxCacheSize = 10000;

    private long currentCacheBeginIndex;

    private long cacheRangeBeginIndex;

    private long cacheRangeEndIndex;

    private Object[] cacheList;

    public CachedStore(Store<T> store, int maxCacheSize, long cacheRangeBeginIndex, long cacheRangeEndIndex) {
        this.store = store;
        this.maxCacheSize = maxCacheSize;
        this.cacheRangeBeginIndex = cacheRangeBeginIndex;
        this.cacheRangeEndIndex = cacheRangeEndIndex;

        this.currentCacheBeginIndex = cacheRangeBeginIndex;
        int realCacheSize = Math.min(maxCacheSize, Long.valueOf(cacheRangeEndIndex - cacheRangeBeginIndex).intValue() + 1);
        realCacheSize = Math.min(realCacheSize, Long.valueOf(store.size()).intValue());
        this.cacheList = new Object[realCacheSize];

        List<T> dataList = store.get(currentCacheBeginIndex, cacheList.length);
        if(dataList.size() != cacheList.length){
            throw new IllegalArgumentException(String.format("dataList.size()[%s] != cacheList.length[%s]", dataList.size(), cacheList.length));
        }
        for (int i = 0; i < dataList.size(); i++) {
            cacheList[i] = dataList.get(i);
        }
    }

    @Override
    public String name() {
        return store.name();
    }

    @Override
    public CachedStore<T> fork(String name, long size) {
        return new CachedStore<>(store.fork(name, size), maxCacheSize, 0, size-1);
    }

    @Override
    public void close() {
        if(cacheList == null){
            return;
        }
        flush();
        cacheList = null;
        IOUtils.closeQuietly(store);
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
        return index >= currentCacheBeginIndex && index < currentCacheBeginIndex + cacheList.length;
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
        long maxCacheBeginIndex = cacheRangeEndIndex - cacheList.length + 1;
        currentCacheBeginIndex = Math.min(maxCacheBeginIndex, index);
        List<T> dataList = store.get(currentCacheBeginIndex, cacheList.length);
        for (int i = 0; i < cacheList.length; i++) {
            cacheList[i] = dataList.get(i);
        }

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("moveCache@{} to {}, cause {}", hashCode() + ":" + name(), currentCacheBeginIndex, index);
        }
    }

    @Override
    public List<T> get(long index, long length) {
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
        ArrayList<T> al = new ArrayList<>(cacheList.length);
        for (Object o : cacheList) {
            al.add((T)o);
        }
        store.set(currentCacheBeginIndex, al);
    }
}
