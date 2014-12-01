package org.sllx.site.blog.service;

import org.sllx.core.Page;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.entity.Articleclass;
import org.sllx.site.core.base.BaseService;

import java.util.List;

/**
 * Created by sllx on 14-11-28.
 */
public interface BlogService extends BaseService<Article>{

    Article getFull(Article obj);

    List<Article> listFull(Article obj, Page page);

    List<Articleclass> listArticleclass(Articleclass userid);

    void save(Article article);
}