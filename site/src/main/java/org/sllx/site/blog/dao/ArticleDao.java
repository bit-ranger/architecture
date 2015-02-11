package org.sllx.site.blog.dao;

import org.sllx.mvc.Dao;
import org.sllx.site.blog.entity.Article;

import java.util.List;

public interface ArticleDao extends Dao<Article> {

    Article getFull(Article article);

    List<Article> listFull(Article article);

}
