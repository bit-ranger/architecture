package org.sllx.user.controller;

import org.sllx.core.controller.BaseController;
import org.sllx.core.support.Page;
import org.sllx.user.entity.User;
import org.sllx.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.List;

@Controller
@RequestMapping("/home")
public class Home extends BaseController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping
    public String list(ServletRequest request, Page page, ModelMap model){
        List<User> users = userService.list(makeParam(request), page);
        model.addAttribute("users", users);
        return "hello";
    }

    @RequestMapping("/get")
    public String get(User user, ModelMap model){
        user = userService.get(user);
        model.addAttribute("user", user);
        return "hello";
    }


    @RequestMapping("/add")
    public String add(User user){
        userService.insert(user);
        return "redirect:/home.s";
    }

    @RequestMapping("/edit")
    public String edit(User user){
        User dbo = userService.get(user);
        userService.copyValidProp(dbo, user);
        userService.update(dbo);
        return "redirect:/home.s";
    }

    @RequestMapping("/delete")
    public String delete(User user){
        userService.delete(user);
        return "redirect:/home.s";
    }

    @RequestMapping("/getJson")
    public @ResponseBody User getJson(User user){
        return userService.get(user);
    }
}
