package org.sllx.site.blog.dao.impl;

import org.sllx.mvc.DaoMyBatisSupport;
import org.sllx.site.blog.dao.ArticleDao;
import org.sllx.site.blog.entity.Article;

import java.util.List;

public class ArticleDaoImpl extends DaoMyBatisSupport<Article> implements ArticleDao{
    @Override
    public Article getFull(Article article) {
       return (Article)sqlSession.selectOne(makeIdFullName("selectFull"), article);
    }

    @Override
    public List<Article> listFull(Article article) {
        return sqlSession.selectList(makeIdFullName("selectFull"), article);
    }
}
