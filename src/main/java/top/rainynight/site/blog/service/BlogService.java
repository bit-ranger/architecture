package top.rainynight.site.blog.service;

import top.rainynight.foundation.util.Page;
import top.rainynight.foundation.Service;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;

import java.util.List;

public interface BlogService extends Service<Article> {

    Article getFull(Article obj);

    List<Article> listFull(Article obj, Page page);

    List<Articleclass> listArticleclass(Articleclass userid);

    void save(Article article);
}