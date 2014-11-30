package org.sllx.site.blog.service;

import org.sllx.site.blog.entity.Article;
import org.sllx.site.core.base.BaseService;

import java.util.List;

/**
 * Created by sllx on 14-11-28.
 */
public interface BlogService extends BaseService<Article>{
    Article getExpand(Article article);
    List<Article> listExpand(Article article);
}
