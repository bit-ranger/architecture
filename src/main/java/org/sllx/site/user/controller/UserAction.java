package org.sllx.site.user.controller;

import org.sllx.site.core.controller.BaseController;
import org.sllx.site.core.support.Common;
import org.sllx.site.core.support.Page;
import org.sllx.site.user.entity.User;
import org.sllx.site.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserAction extends BaseController {

    private static final String REDIRECT = "redirect:/user";

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(User user, Page page, ModelMap model) throws IllegalAccessException {
        List<User> users = userService.list(null, page);
        model.addAttribute("users", users);
        return "user";
    }

    @RequestMapping(value  = "/{id}", method = RequestMethod.GET)
    public String get(@PathVariable int id, ModelMap model){
        User user = userService.get(new User(id));
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post(User user){
        userService.insert(user);
        return REDIRECT;
    }

    @RequestMapping(value  = "/{id}", method = RequestMethod.PUT)
    public String put(@PathVariable int id, User user){
        user.setId(id);
        User dbo = userService.get(user);
        userService.copyValidProp(dbo, user);
        userService.update(dbo);
        return REDIRECT;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable int id){
        userService.delete(new User(id));
        return REDIRECT;
    }

    @RequestMapping(value  = "/{id}/json", method = RequestMethod.TRACE)
    public @ResponseBody User json(@PathVariable int id){
        return userService.get(new User(id));
    }

    @RequestMapping("/file")
    public String upload(@RequestParam MultipartFile uploadFile) throws IOException {
        uploadFile.transferTo(new File("/home/sllx/tmp/" + uploadFile.getOriginalFilename()));
        return REDIRECT;
    }
}
