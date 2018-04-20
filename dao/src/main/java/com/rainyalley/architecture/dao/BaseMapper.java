package com.rainyalley.architecture.dao;


import com.rainyalley.architecture.core.Identical;

/**
 * 数据访问顶层接口
 *
 * @param <T>
 */
public interface BaseMapper<T extends Identical> {

    /**
     * 添加一个对象
     *
     * @param obj 将被添加的对象
     * @return 受影响的对象个数
     */
    int insert(T obj);

    /**
     * 删除一个对象
     *
     * @return 受影响的对象个数
     */
    int delete(String id);

    /**
     * 更新一个对象
     *
     * @param obj 将被更新的对象
     * @return 受影响的对象个数
     */
    int update(T obj);

    /**
     * 获取
     * @param id
     * @return
     */
    T get(String id);
}
