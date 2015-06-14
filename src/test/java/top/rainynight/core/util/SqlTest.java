package top.rainynight.core.util;

import org.apache.ibatis.jdbc.SQL;
import org.junit.Test;

/**
 * 动态代码块先于构造方法执行
 */
public class SqlTest {

    @Test
    public void test(){
        System.out.println(new SQL(){{
            SELECT("*");
            FROM("user");
        }}.toString());

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
