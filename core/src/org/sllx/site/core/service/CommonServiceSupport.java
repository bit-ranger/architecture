package org.sllx.site.core.service;


import org.sllx.site.core.dao.Dao;
import org.sllx.site.core.support.Page;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public abstract class CommonServiceSupport<T> implements Service<T>{

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

    /**
     * 复制提供者<code>provider</code>中的有效属性至接受者<code>recipient</code>中
     * <p>若字段的值不为null，将判定此属性有效</p>
     * @param recipient 接受者
     * @param provider 提供者
     * @return 复制属性后的接受者
     */
    public <T> T  copyValidProp(T recipient ,T provider){
        try{
            Class<?> c = recipient.getClass();
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object pv = field.get(provider);
                if(pv != null){
                    field.set(recipient, pv);
                }
            }
        } catch (IllegalAccessException e){
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return recipient;
    }
}
