package com.rainyalley.architecture.arithmetic.sort;

import java.io.Closeable;
import java.util.List;

/**
 * @author bin.zhang
 * 随机访问存储器
 */
public interface Store<T extends Comparable<T>> extends Closeable{

    /**
     * 名称
     * @return name
     */
    String name();

    /**
     * 创建一个新的
     * 除了name, size之外，一切特性与宿主相同
     * @param name 名称
     * @param size 容量
     * @return 新的Store
     */
    Store<T> fork(String name, long size);

    /**
     * 删除
     * @return true删除成功, false删除失败
     */
    boolean delete();

    /**
     * 获取一个
     * @param index
     * @return 元素
     */
    T get(long index);

    /**
     * 获取多个
     * @param index 位置
     * @param length 数量
     * @return 元素列表
     */
    List<T> get(long index, long length);

    /**
     * 设置
     * @param index
     * @param data
     */
    void set(long index, T data);

    /**
     * 设置多个
     * @param index 位置
     * @param dataList 数据集
     */
    void set(long index, List<T> dataList);

    /**
     * 数据拷贝
     * @param descIndex 目标位置
     * @param src 来源
     * @param srcIndex 来源中的位置
     * @param length 来源中的数量
     */
    void copyFrom(long descIndex, Store<T> src, long srcIndex, long length);

    /**
     * 注意，这不是实际存储的元素数量
     * @return 容量
     */
    long size();
}
