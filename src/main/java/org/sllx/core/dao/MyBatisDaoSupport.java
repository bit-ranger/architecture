package org.sllx.core.dao;


import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.sllx.core.log.Logger;
import org.sllx.core.log.LoggerFactory;
import org.sllx.core.support.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public  class MyBatisDaoSupport<T>  implements Dao<T>{
    private static Logger logger = LoggerFactory.getLogger(MyBatisDaoSupport.class);

    protected final static String SQLID_INSERT = "insert";
    protected final static String SQLID_DELETE = "delete";
    protected final static String SQLID_UPDATE = "update";
    protected final static String SQLID_GET = "get";
    protected final static String SQLID_LIST = "list";
    protected final static int ERROR_CODE = -1;


    protected SqlSessionTemplate sqlSessionTemplate;

    private String genericClassName;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
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
                throw new ClassCastException("泛型必须为确定的类型!");
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

    public int insert(T obj){
        try{
            return sqlSessionTemplate.insert(getNameSpace() + "." + SQLID_INSERT, obj);
        } catch (Exception e){
            handle(e);
            return ERROR_CODE;
        }
    }

    public int delete(T obj){
        try {
            return sqlSessionTemplate.delete(getNameSpace() + "." + SQLID_DELETE, obj);
        } catch (Exception e){
            handle(e);
            return ERROR_CODE;
        }
    }

    public int update(T obj){
        try{
            return sqlSessionTemplate.update(getNameSpace() + "." + SQLID_UPDATE, obj);
        } catch (Exception e){
            handle(e);
            return ERROR_CODE;
        }
    }

    public T get(T obj){
        try{
            return sqlSessionTemplate.selectOne(getNameSpace() + "." + SQLID_GET, obj);
        } catch (Exception e){
            handle(e);
            return null;
        }
    }

    public List<T> list(Map<String, String> param, Page page){
        try{
            return sqlSessionTemplate.selectList(getNameSpace() + "." + SQLID_LIST, toWhereClause(param), new RowBounds(page.getOffset(), page.getPageSize()));
        } catch (Exception e){
            handle(e);
            return new ArrayList<T>(0);
        }
    }

    private String toWhereClause(Map<String,String> param){
        StringBuilder clause = new StringBuilder();
        if(param != null && param.size() > 0){
            int index = 0;
            for (Map.Entry<String,String> entry : param.entrySet()) {
                String value = entry.getValue();
                if(value == null || value.equals("")){
                    continue;
                }
                String key = entry.getKey();
                if(index > 0){
                    clause.append(" and");
                }
                clause.append(" ").append(key).append("=").append("'").append(value).append("'");
                index ++;
            }
        }
        if(clause.length() > 0){
            clause.insert(0, " where");
        }
        return clause.toString();
    }

    private void handle(Throwable e){
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e);
    }
}
