package org.sllx.site.blog.dao.impl;

import org.sllx.site.blog.dao.ArticleDao;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.core.base.BaseDaoImpl;

import java.util.List;

/**
 * Created by sllx on 14-11-28.
 */
public class ArticleDaoImpl extends BaseDaoImpl<Article> implements ArticleDao{
    @Override
    public Article getFull(Article article) {
       return (Article)sqlSession.selectOne(makeIdFullName("selectFull"), article);
    }

    @Override
    public List<Article> listFull(Article article) {
        return sqlSession.selectList(makeIdFullName("selectFull"), article);
    }
}
