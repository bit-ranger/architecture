package top.rainynight.site.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import top.rainynight.core.util.Page;
import top.rainynight.site.blog.entity.Article;
import top.rainynight.site.blog.entity.Articleclass;
import top.rainynight.site.blog.service.BlogService;
import top.rainynight.site.core.util.StaticResourceHolder;
import com.rainyalley.common.user.model.entity.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@Controller
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
    public String release(@Valid Article article, BindingResult bindingResult, HttpSession session, ModelMap modelMap){
        if(bindingResult.hasErrors()){
            return editor(new Articleclass(),modelMap);
        }
        modelMap.clear();
        Object userObj = session.getAttribute(StaticResourceHolder.USER_SESSION_NAME);
        if(userObj == null){
            return "redirect:/login";
        }
        User user = (User)userObj;
        article.setUserid(user.getId());
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
