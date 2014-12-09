package org.sllx.site.blog.dao;

import org.sllx.site.blog.entity.Article;
import org.sllx.site.core.base.BaseDao;

import java.util.List;

public interface ArticleDao extends BaseDao<Article>{

    Article getFull(Article article);

    List<Article> listFull(Article article);

}
