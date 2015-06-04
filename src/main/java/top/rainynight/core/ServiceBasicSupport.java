package top.rainynight.core;


import top.rainynight.core.util.Page;

import java.util.List;

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
    public int save(T obj) {
        int count = 0;
        List<T> pojoList = dao.select(obj,null);
        if(pojoList == null || pojoList.isEmpty()){
            count = dao.insert(obj);
        }else{
            count = dao.update(obj);
        }
        return count;
    }

    @Override
    public int remove(T obj) {
        return dao.delete(obj);
    }

    @Override
    public T get(T obj) {
        List<T> pojoList = dao.select(obj, null);
        if(pojoList == null || pojoList.isEmpty()){
            return null;
        }
        return pojoList.get(0);
    }

    @Override
    public List<T> get(T obj, Page page) {
        return dao.select(obj,page);
    }
}
