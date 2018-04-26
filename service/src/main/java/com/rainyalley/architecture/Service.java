package com.rainyalley.architecture;


/**
 * 事务顶层接口
 *
 * @param <T>
 */
public interface Service<T extends Identical> {
    /**
     * 保存一个对象
     *
     * @param obj 将被添加的对象
     * @return 受影响的对象个数
     */
    T save(T obj);

    /**
     * 删除一个对象
     *
     * @param id 将被删除的对象
     * @return 受影响的对象个数
     */
    int remove(String id);

    /**
     * 获取一个对象
     *
     * @return 获取的对象
     */
    T get(String id);
}
