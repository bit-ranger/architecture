package com.rainyalley.architecture.core.arithmetic.sort;

import java.util.List;

public interface ExternalStore<T extends Comparable<T>> {

    /**
     * 名称
     * @return name
     */
    String name();

    /**
     * 创建一个新的
     * @param name 名称
     * @param size 容量
     * @return
     */
    ExternalStore<T> create(String name, long size);

    void close();

    T get(long index);

    List<T> get(long index, long length);

    void set(long index, T data);

    void set(long index, List<T> dataList);

    void copyFrom(long descIndex, ExternalStore<T> src, long srcIndex, long length);

    long size();
}
