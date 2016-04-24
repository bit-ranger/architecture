package com.rainyalley.architecture.web.user;

import com.rainyalley.architecture.common.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping
    public String index(){
        return "";
    }

    @RequestMapping("404")
    public String redirect404(ModelMap modelMap){
        modelMap.clear();
        return "redirect:/";
    }

}
