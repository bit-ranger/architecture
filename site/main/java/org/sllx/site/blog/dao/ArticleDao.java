package org.sllx.site.blog.dao;

import org.sllx.site.blog.entity.Article;
import org.sllx.site.core.base.BaseDao;

import java.util.List;

/**
 * Created by sllx on 14-11-28.
 */
public interface ArticleDao extends BaseDao<Article>{

    Article getExpand(Article article);

    List<Article> listExpand(Article article);

}
