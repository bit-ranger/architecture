package org.sllx.core.dao;

import org.sllx.core.support.Page;

import java.util.List;
import java.util.Map;

public interface Dao<T> {
    int insert(T obj);
    int delete(T obj);
    int update(T obj);
    T get(T obj);
    List<T> list(Map<String,String> param, Page page);
}
