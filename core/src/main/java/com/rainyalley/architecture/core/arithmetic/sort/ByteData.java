package com.rainyalley.architecture.core.arithmetic.sort;

public interface ByteData<T extends Comparable<T>>{

    byte[] toByteArray(T data);

    T toData(byte[] dataBytes);

    int unitBytes();
}
