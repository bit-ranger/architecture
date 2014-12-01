package org.sllx.site.blog.controller;

import org.sllx.core.Page;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.entity.Articleclass;
import org.sllx.site.blog.service.BlogService;
import org.sllx.site.core.base.BaseController;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@org.springframework.stereotype.Controller
@RequestMapping("blog")
public class BlogController extends BaseController {

    @javax.annotation.Resource(name = "blogService")
    private BlogService blogService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Page page,ModelMap modelMap){
        Article article = new Article();
        article.setUserid(1);
        List<Article> articleList = blogService.list(article,page);
        modelMap.addAttribute("articleList",articleList);
        return "blog/list";
    }

    @RequestMapping(value = "editor", method = RequestMethod.GET)
    public String editor(Articleclass articleclass,ModelMap modelMap){
        articleclass.setUserid(1);
        List<Articleclass> articleclassList = blogService.listArticleclass(articleclass);
        modelMap.addAttribute("articleclassList",articleclassList);
        modelMap.addAttribute("uploadHref",linkTo(FileController.class).withSelfRel().getHref());
        modelMap.addAttribute("releaseHref",selfLinkBuilder.slash("release").withRel("release").getHref());
        return "blog/editor";
    }

    @RequestMapping(value = "release", method = RequestMethod.POST)
    public String release(Article article,ModelMap modelMap){
        article.setUserid(1);
        article.setReleasetime(new Date());
        article.setSort(1);
        article.setState(0);
        blogService.save(article);
        modelMap.clear();
        return "redirect:/blog";
    }

}
