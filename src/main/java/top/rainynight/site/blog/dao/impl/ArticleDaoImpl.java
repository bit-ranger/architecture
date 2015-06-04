package top.rainynight.site.blog.dao.impl;

import top.rainynight.core.DaoMyBatisSupport;
import top.rainynight.site.blog.dao.ArticleDao;
import top.rainynight.site.blog.entity.Article;

import java.util.List;


public class ArticleDaoImpl extends DaoMyBatisSupport<Article,ArticleDao> implements ArticleDao{
    @Override
    public List<Article> listWithArticleclass() {
        return getSqlSession().getMapper(mapper()).listWithArticleclass();
    }
}
