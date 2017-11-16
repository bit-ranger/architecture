package com.rainyalley.architecture.service;


import java.util.List;
import java.util.Map;

/**
 * 数据访问顶层接口
 *
 * @param <T>
 */
public interface Dao<T> {

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
     * @param obj 将被删除的对象
     * @return 受影响的对象个数
     */
    int delete(T obj);

    /**
     * 更新一个对象
     *
     * @param obj 将被更新的对象
     * @return 受影响的对象个数
     */
    T update(T obj);

    /**
     * 获取一组对象
     *
     * @return 对象列表
     */
    List<T> select(Map<String, Object> params);
}
