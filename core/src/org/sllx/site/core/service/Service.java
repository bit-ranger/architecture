package org.sllx.site.core.service;


import org.sllx.site.core.support.Page;

import java.util.List;
import java.util.Map;

public interface Service<T> {
    int insert(T obj);
    int delete(T obj);
    int update(T obj);
    T get(T obj);
    List<T> list(Map<String,String> param, Page page);
}
