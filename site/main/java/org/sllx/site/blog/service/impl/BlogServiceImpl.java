package org.sllx.site.blog.service.impl;

import org.sllx.site.blog.dao.ArticleDao;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.service.BlogService;
import org.sllx.site.core.base.BaseServiceImpl;

import java.util.List;

/**
 * Created by sllx on 14-11-28.
 */
public class BlogServiceImpl extends BaseServiceImpl<Article> implements BlogService{

    private ArticleDao ardticleDao;

    public void setArdticleDao(ArticleDao ardticleDao) {
        setDao(ardticleDao);
        this.ardticleDao = ardticleDao;
    }

    @Override
    public Article getExpand(Article article) {
        return ardticleDao.getExpand(article);
    }

    @Override
    public List<Article> listExpand(Article article) {
        return ardticleDao.listExpand(article);
    }
}
