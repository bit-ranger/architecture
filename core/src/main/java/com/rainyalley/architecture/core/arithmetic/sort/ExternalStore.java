package com.rainyalley.architecture.core.arithmetic.sort;

public interface ExternalStore<T extends Comparable<T>> {

    String name();

    ExternalStore<T> create(String name, long size);

    void delete();

    T get(long index);

    void set(long index, T data);

    long size();
}
