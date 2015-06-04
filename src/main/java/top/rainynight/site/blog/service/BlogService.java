package top.rainynight.site.blog.service;

import top.rainynight.core.Service;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;

import java.util.List;

public interface BlogService extends Service<Article> {

    List<Articleclass> listArticleclass(Articleclass userid);

}