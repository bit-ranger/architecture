package org.sllx.mvc;


import org.sllx.core.util.Page;

import java.util.List;
import java.util.Map;

/**
 * 事务顶层接口
 * @param <T>
 */
public interface Service<T> {
    int insert(T obj);
    int delete(T obj);
    int update(T obj);
    T get(T obj);
    List<T> list(Map<String,String> param, Page page);
}
