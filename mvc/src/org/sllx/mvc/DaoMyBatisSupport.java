package org.sllx.mvc;


import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.sllx.core.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * 实现了{@link Dao}中所有方法的默认实现类
 * 当使用Mybatis框架时该类可作为DAO的通用实现
 * @param <T>
 */
public abstract class DaoMyBatisSupport<T>  implements Dao<T> {

    protected final static String SQLID_INSERT = "insert";
    protected final static String SQLID_DELETE = "delete";
    protected final static String SQLID_UPDATE = "update";
    protected final static String SQLID_SELECT = "select";

    protected SqlSession sqlSession;

    private String genericClassName;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * @return 泛型实际类型的名称
     */
    private String getGenericClassName() {
        if (genericClassName != null) {
            return genericClassName;
        }
        ParameterizedType paramType = getParameterizedType(this.getClass());
        genericClassName = getName(paramType);
        return genericClassName;
    }

    /**
     * @param clazz 起点类型
     * @return 参数化的父类类型
     */
    private ParameterizedType getParameterizedType(Class<?> clazz){
        Type st = clazz.getGenericSuperclass();
        if(st instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType)st;
            if(getName(pt) == null){
                throw new ClassCastException(String.format("the parameterizedType [%s] must be clear!",this.getClass().getName()));
            }
            return pt;
        } else{
            return getParameterizedType(clazz.getSuperclass());
        }
    }

    /**
     * @param pt 参数化父类类型
     * @return 参数化的的父类中实际参数类型的名称
     */
    private String getName(ParameterizedType pt){
        Type param = pt.getActualTypeArguments()[getGenericIndex()];
        if(param instanceof Class){
            return ((Class<?>) param).getName();
        } else {
            return null;
        }
    }

    /**
     * @return 命名空间
     */
    protected String getNameSpace(){
        return getGenericClassName();
    }

    /**
     * @return 需要的泛型的索引
     */
    protected int getGenericIndex(){
        return 0;
    }

    protected String makeIdFullName(String sqlid){
        return getNameSpace() + "." + sqlid;
    }

    public int insert(T obj){
        return sqlSession.insert(makeIdFullName(SQLID_INSERT), obj);
    }

    public int delete(T obj){
        return sqlSession.delete(makeIdFullName(SQLID_DELETE), obj);
    }

    public int update(T obj){
        return sqlSession.update(makeIdFullName(SQLID_UPDATE), obj);
    }

    public T get(T obj){
        return (T)sqlSession.selectOne(makeIdFullName(SQLID_SELECT), obj);
    }

    public List<T> list(T obj, Page page){
        return sqlSession.selectList(makeIdFullName(SQLID_SELECT), obj, new RowBounds(page.getOffset(), page.getPageSize()));
    }
}
