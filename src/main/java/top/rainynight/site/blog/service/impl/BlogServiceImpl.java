package top.rainynight.site.blog.service.impl;

import top.rainynight.foundation.util.Page;
import top.rainynight.foundation.ServiceBasicSupport;
import top.rainynight.site.blog.dao.ArticleDao;
import top.rainynight.site.blog.dao.ArticleclassDao;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;
import top.rainynight.site.blog.service.BlogService;

import java.util.List;

public class BlogServiceImpl extends ServiceBasicSupport<Article> implements BlogService {

    private ArticleclassDao articleclassDao;

    private ArticleDao ardticleDao;

    public void setArdticleDao(ArticleDao ardticleDao) {
        setDao(ardticleDao);
        this.ardticleDao = ardticleDao;
    }

    public void setArticleclassDao(ArticleclassDao articleclassDao) {
        this.articleclassDao = articleclassDao;
    }

    @Override
    public Article getFull(Article article) {
        return ardticleDao.getFull(article);
    }

    @Override
    public List<Article> listFull(Article article,Page page) {
        return ardticleDao.listFull(article);
    }

    @Override
    public List<Articleclass> listArticleclass(Articleclass articleclass) {
        return articleclassDao.list(articleclass,new Page());
    }

    @Override
    public void save(Article article) {
        ardticleDao.insert(article);
    }
}
