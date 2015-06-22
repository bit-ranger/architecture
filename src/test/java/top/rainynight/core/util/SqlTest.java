package top.rainynight.core.util;

import org.apache.ibatis.jdbc.SQL;
import org.junit.Test;

/**
 * 动态代码块先于构造方法执行
 */
public class SqlTest {

    @Test
    public void test(){
        String sql = new SQL(){{
            SELECT("name");
            SELECT("password");
            SELECT("sex");
            FROM("student s");
            INNER_JOIN("info i on s.id = i.id");
            WHERE(String.format("name = '%s'","HanMeiMei"));
            WHERE("i.sex = 1");
        }}.toString();

        System.out.println(sql);
        new ExSQL();
    }

    private static class ExSQL extends  SQL{

        static {
            System.out.println("static");
        }

        String s = new Object(){
            @Override
            public String toString() {
                System.out.println("field");
                return super.toString();
            }
        }.toString();

        {
            System.out.println("dynamic");
        }
        ExSQL(){
            System.out.println("construct");
        }
    }

}
