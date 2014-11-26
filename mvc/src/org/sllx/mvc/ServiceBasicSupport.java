package org.sllx.mvc;


import org.sllx.core.Page;

import java.util.List;
import java.util.Map;

/**
 * 实现了{@link Service}中所有方法的默认实现类
 * 该类可作为事务的通用实现
 * @param <T>
 */
public abstract class ServiceBasicSupport<T> implements Service<T> {

    private Dao<T> dao;

    public void setDao(Dao<T> dao) {
        this.dao = dao;
    }

    @Override
    public int insert(T obj) {
        return dao.insert(obj);
    }

    @Override
    public int delete(T obj) {
        return dao.delete(obj);
    }

    @Override
    public int update(T obj) {
        return dao.update(obj);
    }

    @Override
    public T get(T obj) {
        return dao.get(obj);
    }

    @Override
    public List<T> list(Map<String,String> param, Page page) {
        return dao.list(param,page);
    }
}
