package com.rainyalley.architecture.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * RedisSearchServiceImpl Tester.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-search.xml")
public class RedisSearchServiceImplTest {


    @Resource(name = "searchService")
    private SearchService searchService;


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: index(String text)
     */
    @Test
    public void testIndex() throws Exception {
        for (int i = 0; i < 10; i++) {
            this.searchService.index(String.valueOf(i), "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!");

        }
    }

    /**
     * Method: search(String keyword, Class<T> type)
     */
    @Test
    public void testSearch() throws Exception {
        Set<String> idSet = this.searchService.search("a");
        System.out.println(idSet);
    }


// private methods ~~~~


}
