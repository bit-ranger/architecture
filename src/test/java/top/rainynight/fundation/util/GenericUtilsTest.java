package top.rainynight.fundation.util;

import org.junit.Test;
import top.rainynight.core.util.GenericUtils;
import top.rainynight.site.blog.dao.impl.ArticleclassDaoImpl;
import top.rainynight.site.blog.entity.Articleclass;

/**
 * Created by sllx on 2015-06-04.
 */
public class GenericUtilsTest {

    @Test
    public void getActualClass(){
        Class cla =  GenericUtils.getActualClass(ArticleclassDaoImpl.class,0);
        Articleclass.class.equals(cla);
    }

}
