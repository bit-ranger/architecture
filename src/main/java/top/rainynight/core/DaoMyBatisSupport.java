package top.rainynight.core;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import top.rainynight.core.util.GenericUtils;
import top.rainynight.core.util.Page;
import java.util.List;


/**
 * 实现了{@link Dao}中所有方法的默认实现类
 * 当使用Mybatis框架时该类可作为DAO的通用实现
 * @param <T>
 */
public abstract class DaoMyBatisSupport<T,M extends Dao<T>>  implements Dao<T> {


    private SqlSession sqlSession;
    private Class<M> mapperClass;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    protected int mapperIndex(){
        return 1;
    }

    protected Class<M> mapper(){
        if(mapperClass == null){
             mapperClass = (Class<M>)GenericUtils.getActualClass(this.getClass(),mapperIndex());
        }
        return mapperClass;
    }

    public int insert(T obj){
        return sqlSession.getMapper(mapper()).insert(obj);
    }

    public int delete(T obj){
        return sqlSession.getMapper(mapper()).delete(obj);
    }

    public int update(T obj){
        return sqlSession.getMapper(mapper()).update(obj);
    }

    public List<T> select(T obj, Page page){
        return sqlSession.getMapper(mapper()).select(obj,page);
    }

}
