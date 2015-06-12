package top.rainynight.core;

import org.springframework.transaction.annotation.Transactional;
import top.rainynight.core.util.BeanMapConvertor;
import top.rainynight.core.util.Page;

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
    @Transactional
    public int save(T obj) {
        int count = 0;
        List<T> pojoList = _get(obj, new Page());
        if(pojoList == null || pojoList.isEmpty()){
            count = dao.insert(obj);
        }else{
            count = dao.update(obj);
        }
        return count;
    }

    @Override
    @Transactional
    public int remove(T obj) {
        return dao.delete(obj);
    }

    @Override
    @Transactional(readOnly = true)
    public T get(T obj) {
        List<T> pojoList = _get(obj, new Page());
        if(pojoList == null || pojoList.isEmpty()){
            return null;
        }
        return pojoList.get(0);
    }

    private List<T> _get(T obj, Page page) {
        Map<String,Object> params = BeanMapConvertor.merge(obj,page);
        return dao.select(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> get(T obj, Page page){
        return _get(obj,page);
    }
}
