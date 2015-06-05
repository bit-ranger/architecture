package top.rainynight.site.blog.service.impl;

import org.springframework.stereotype.Service;
import top.rainynight.core.util.BeanMapConvertor;
import top.rainynight.core.util.Page;
import top.rainynight.core.ServiceBasicSupport;
import top.rainynight.site.blog.dao.ArticleDao;
import top.rainynight.site.blog.dao.ArticleclassDao;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;
import top.rainynight.site.blog.service.BlogService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("blogService")
public class BlogServiceImpl extends ServiceBasicSupport<Article> implements BlogService {

    private ArticleclassDao articleclassDao;

    private ArticleDao articleDao;

    @Resource
    public void setArticleDao(ArticleDao articleDao) {
        setDao(articleDao);
        this.articleDao = articleDao;
    }

    @Resource
    public void setArticleclassDao(ArticleclassDao articleclassDao) {
        this.articleclassDao = articleclassDao;
    }

    @Override
    public List<Articleclass> listArticleclass(Articleclass articleclass) {
        Map<String,Object> params = BeanMapConvertor.merge(articleclass, new Page());
        return articleclassDao.select(params);
    }
}
