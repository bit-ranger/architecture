package top.rainynight.site.blog.controller;

import top.rainynight.core.util.Page;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;
import top.rainynight.site.blog.service.BlogService;
import top.rainynight.site.core.util.StaticResourceHolder;
import top.rainynight.site.user.entity.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Controller
@RequestMapping("blog")
public class BlogController{

    @javax.annotation.Resource(name = "blogService")
    private BlogService blogService;

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.HEAD})
    public String list(Page page, ModelMap modelMap){
        Article article = new Article();
        List<Article> articleList = blogService.get(article,page);
        modelMap.addAttribute("articleList",articleList);
        return "blog/list";
    }

    @RequestMapping(value = "editor", method = RequestMethod.GET)
    public String editor(Articleclass articleclass,ModelMap modelMap){
        articleclass.setUserid(1);
        List<Articleclass> articleclassList = blogService.listArticleclass(articleclass);
        modelMap.addAttribute("articleclassList",articleclassList);
        return "blog/editor";
    }

    @RequestMapping(value = "release", method = RequestMethod.POST)
    public String release(Article article, HttpSession session, ModelMap modelMap){
        modelMap.clear();
        Object userObj = session.getAttribute(StaticResourceHolder.USER_SESSION_NAME);
        if(userObj == null){
            return "redirect:/login";
        }
        User user = (User)userObj;
        article.setUserid(user.getUserid());
        article.setReleasetime(new Date());
        blogService.save(article);
        return "redirect:/";
    }

    @RequestMapping(value="{id:[0-9]{1,9}}" , method = RequestMethod.GET)
    public String view(@PathVariable Integer id, Article article, ModelMap modelMap){
        article.setArticleid(id);
        article = blogService.get(article);
        if(article == null){
            modelMap.clear();
            return "redirect:/404";
        }
        modelMap.addAttribute("article", article);
        return "blog/view";
    }

}
