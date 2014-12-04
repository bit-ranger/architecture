package org.sllx.site.blog.controller;

import org.sllx.core.Page;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.blog.entity.Articleclass;
import org.sllx.site.blog.service.BlogService;
import org.sllx.site.core.GlobalController;
import org.sllx.site.core.base.BaseController;
import org.sllx.site.user.entity.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.print.resources.serviceui_es;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Controller
@RequestMapping("blog")
public class BlogController extends BaseController {

    @javax.annotation.Resource(name = "blogService")
    private BlogService blogService;


    @RequestMapping(method = RequestMethod.GET)
    public String list(Page page, ModelMap modelMap){
        Article article = new Article();
        List<Article> articleList = blogService.list(article,page);
        modelMap.addAttribute("articleList",articleList);
        modelMap.addAttribute("selfURL",selfURL());
        return "blog/list";
    }

    @RequestMapping(value = "editor", method = RequestMethod.GET)
    public String editor(Articleclass articleclass,ModelMap modelMap){
        articleclass.setUserid(1);
        List<Articleclass> articleclassList = blogService.listArticleclass(articleclass);
        modelMap.addAttribute("articleclassList",articleclassList);
        modelMap.addAttribute("loginURL", url(GlobalController.class,"login","loginURL"));
        modelMap.addAttribute("uploadURL", url(FileController.class,"uploadURL"));
        modelMap.addAttribute("releaseURL",selfURL("release","releaseURL"));
        return "blog/editor";
    }

    @RequestMapping(value = "release", method = RequestMethod.POST)
    public String release(Article article, HttpSession session){
        Object userObj = session.getAttribute("user");
        if(userObj == null){
            return "redirect:/login";
        }
        User user = (User)userObj;
        article.setUserid(user.getUserid());
        article.setReleasetime(new Date());
        article.setSort(1);
        article.setState(0);
        blogService.save(article);
        return "redirect:/";
    }

    @RequestMapping(value="{id:[0-9]{1,9}}" , method = RequestMethod.GET)
    public String view(@PathVariable Integer id, Article article, ModelMap modelMap){
        article.setArticleid(id);
        article = blogService.getFull(article);
        if(article == null){
            return "redirect:/404";
        }
        modelMap.addAttribute("article", article);
        return "blog/view";
    }

}
