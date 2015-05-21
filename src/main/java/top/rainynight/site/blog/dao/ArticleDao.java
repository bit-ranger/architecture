package top.rainynight.site.blog.dao;

import top.rainynight.foundation.Dao;
import top.rainynight.site.blog.entity.Article;

import java.util.List;

public interface ArticleDao extends Dao<Article> {

    Article getFull(Article article);

    List<Article> listFull(Article article);

}
