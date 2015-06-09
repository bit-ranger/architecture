package top.rainynight.site.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import top.rainynight.site.blog.dao.ArticleDao;
import top.rainynight.site.blog.dao.ArticleclassDao;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;
import top.rainynight.site.user.dao.UserDao;
import top.rainynight.site.user.entity.User;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

/**
 * 必须注意继承的类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback = true) //可选，默认就是这个
public class ArticleDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    private ArticleDao articleDao;

    @Resource
    private ArticleclassDao articleclassDao;

    @Resource
    private UserDao userDao;

    private User user = new User();

    private Articleclass articleclass = new Articleclass();

    private Article article = new Article();

    public ArticleDaoTest(){
        setUser();
        setArticleclass(user);
        setArticle(user, articleclass);
    }


    private void setUser(){
        user.setName("测试");
        user.setPassword("123456");
    }

    private void setArticleclass(User user){
        articleclass.setUserid(user.getUserid());
        articleclass.setName("测试");
    }

    private void setArticle(User user, Articleclass articleclass){
        article.setClassid(articleclass.getClassid());
        article.setUserid(user.getUserid());
        article.setTitle("测试");
        article.setContent("测试");
        article.setReleasetime(new Date());
    }

    @Test
    public void listWithArticleclass() {
        articleDao.selectRelevance();
    }

    @Test
    @Rollback
    @Transactional
    public void insert() {
        userDao.insert(user);
        setArticleclass(user);
        articleclassDao.insert(articleclass);
        setArticle(user, articleclass);
        articleDao.insert(article);
    }

    @Test
    @Rollback
    @Transactional
    public void delete() {
        articleDao.delete(article);
    }

    @Test
    @Rollback
    public void update() {
        article.setArticleid(0);
        articleDao.update(article);
    }

    @Test
    public void select() {
        articleDao.select(new HashMap<String, Object>());
    }
}
