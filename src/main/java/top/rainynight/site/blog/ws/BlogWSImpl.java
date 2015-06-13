package top.rainynight.site.blog.ws;


import org.springframework.stereotype.Component;
import top.rainynight.core.util.Page;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.service.BlogService;

import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@Component("blogWS")
@WebService
public class BlogWSImpl implements BlogWS{

    @javax.annotation.Resource(name = "blogService")
    private BlogService blogService;

    @Override
    public List<Article> list() {
        return blogService.get(new Article(), new Page());
    }

    @Override
    public Article get(int id) {
        Article article = new Article();
        article.setArticleid(id);
        return blogService.get(article);
    }

    @Override
    public boolean post(String title, String content, int classId) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setReleasetime(new Date());
        article.setClassid(classId);
        article.setUserid(1);
        return blogService.save(article) > 0;
    }


}
