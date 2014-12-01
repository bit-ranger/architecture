package org.sllx.mvc;

import org.sllx.core.Page;

import java.util.List;
import java.util.Map;

/**
 * 数据访问顶层接口
 * @param <T>
 */
public interface Dao<T> {

    /**
     * 添加一个对象
     * @param obj 将被添加的对象
     * @return 受影响的对象个数
     */
    int insert(T obj);

    /**
     * 删除一个对象
     * @param obj 将被删除的对象
     * @return 受影响的对象个数
     */
    int delete(T obj);

    /**
     * 更新一个对象
     * @param obj 将被更新的对象
     * @return 受影响的对象个数
     */
    int update(T obj);

    /**
     * 获取一个对象
     * @param obj 存放查询信息的对象
     * @return 获取的对象
     */
    T get(T obj);

    /**
     * 获取一组对象
     * @param obj 存放查询信息的对象
     * @param page 分页信息
     * @return 对象列表
     */
    List<T> list(T obj, Page page);
}
