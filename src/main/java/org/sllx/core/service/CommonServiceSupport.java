package org.sllx.core.service;


import org.sllx.core.dao.Dao;
import org.sllx.core.log.Logger;
import org.sllx.core.log.LoggerFactory;
import org.sllx.core.support.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CommonServiceSupport<T> implements Service<T>{
    private static Logger logger = LoggerFactory.getLogger(CommonServiceSupport.class);

    protected final static int ERROR_CODE = -1;

    private Dao<T> dao;

    public void setDao(Dao<T> dao) {
        this.dao = dao;
    }

    @Override
    public int insert(T obj) {
        try {
            return dao.insert(obj);
        } catch (Exception e) {
            handle(e);
            return ERROR_CODE;
        }
    }

    @Override
    public int delete(T obj) {
        try {
            return dao.delete(obj);
        } catch (Exception e) {
            handle(e);
            return ERROR_CODE;
        }
    }

    @Override
    public int update(T obj) {
        try {
            return dao.update(obj);
        } catch (Exception e) {
            handle(e);
            return ERROR_CODE;
        }
    }

    @Override
    public T get(T obj) {
        try {
            return dao.get(obj);
        } catch (Exception e) {
            handle(e);
            return null;
        }
    }

    @Override
    public List<T> list(Map<String,String> param, Page page) {
        try {
            return dao.list(param,page);
        } catch (Exception e) {
            handle(e);
            return  new ArrayList<T>(0);
        }
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
        } catch (Exception e){
            handle(e);
        }
        return recipient;
    }

    private void handle(Throwable e){
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e);
    }
}
