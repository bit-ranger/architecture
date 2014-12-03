package org.sllx.site.core;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sllx on 14-11-28.
 */
@Controller
@RequestMapping("/")
public class IndexController{
    @RequestMapping
    public String index(){
        return "forward:/blog";
    }

    @RequestMapping("404")
    public String redirect404(){
        return "redirect:/";
    }

    /**
     * 跳转至登陆页
     * @param modelMap
     * @return
     */
    @RequestMapping("login")
    public String login(ModelMap modelMap){
        return "/login";
    }

    /**
     * 处理登陆请求
     * @return
     */
    @RequestMapping(value = "landing",method = RequestMethod.POST)
    public String landing(){
        return "redirect:/";
    }
}
