package top.rainynight.site.blog.dao;

import top.rainynight.core.Dao;
import top.rainynight.site.blog.entity.Article;

import java.util.List;

public interface ArticleDao extends Dao<Article> {
    List<Article> selectRelevance();
}
