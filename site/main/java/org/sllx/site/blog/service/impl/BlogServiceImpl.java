package org.sllx.site.blog.service.impl;

import org.sllx.core.Page;
import org.sllx.site.blog.dao.ArticleDao;
import org.sllx.site.blog.dao.ArticleclassDao;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.entity.Articleclass;
import org.sllx.site.blog.service.BlogService;
import org.sllx.site.core.base.BaseServiceImpl;

import java.util.List;

/**
 * Created by sllx on 14-11-28.
 */
public class BlogServiceImpl extends BaseServiceImpl<Article> implements BlogService{

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
