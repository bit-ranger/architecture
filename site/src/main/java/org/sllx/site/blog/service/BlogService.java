package org.sllx.site.blog.service;

import org.sllx.core.Page;
import org.sllx.mvc.Service;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.entity.Articleclass;

import java.util.List;

public interface BlogService extends Service<Article> {

    Article getFull(Article obj);

    List<Article> listFull(Article obj, Page page);

    List<Articleclass> listArticleclass(Articleclass userid);

    void save(Article article);
}