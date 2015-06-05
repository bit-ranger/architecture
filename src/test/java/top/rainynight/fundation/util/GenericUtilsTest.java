package top.rainynight.fundation.util;

import org.junit.Test;
import top.rainynight.core.util.GenericUtils;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.service.impl.BlogServiceImpl;

/**
 * Created by sllx on 2015-06-04.
 */
public class GenericUtilsTest {

    @Test
    public void getActualClass(){
        Class cla =  GenericUtils.getActualClass(BlogServiceImpl.class,0);
        Article.class.equals(cla);
    }

}
