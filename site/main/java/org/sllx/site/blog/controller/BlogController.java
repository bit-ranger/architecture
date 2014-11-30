package org.sllx.site.blog.controller;

import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.service.BlogService;
import org.sllx.site.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("blog")
public class BlogController extends BaseController {

    @javax.annotation.Resource(name = "blogService")
    private BlogService blogService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(){
        Article article = new Article();
        article.setArticleid(1);
        List<Article> articleList = blogService.listExpand(article);
        return "blog/list";
    }

}
