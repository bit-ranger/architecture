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
            SELECT("");
        }}.toString());

        new ExSQL().toString();
    }

    private static class ExSQL extends  SQL{

        static {
            System.out.println("static");
        }

        ExSQL(){
            System.out.println("construct");
        }

        {
            System.out.println("dynamic");
        }
    }
}
