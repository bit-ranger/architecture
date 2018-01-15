package com.rainyalley.architecture.core.arithmetic.sort;

public interface ByteDataConverter<T extends Comparable<T>>{

    /**
     * 数据单元转字节数组
     * @param data
     * @return
     */
    byte[] toByteArray(T data);

    /**
     * 字节数组转数据单元
     * @param dataBytes
     * @return
     */
    T toData(byte[] dataBytes);

    /**
     * 每个数据单元的字节数
     * @return
     */
    int unitBytes();

    /**
     * 数据单元之间的分隔符
     * @return
     */
    byte[] unitSeparator();
}
