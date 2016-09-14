package com.rainyalley.architecture.webapp.user;

import com.rainyalley.architecture.common.user.model.entity.User;
import com.rainyalley.architecture.common.user.service.UserService;
import com.rainyalley.architecture.core.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping
    public List<User> index() {
        return this.userService.get(new User(), new Page());
    }

    @RequestMapping("404")
    public String redirect404(ModelMap modelMap) {
        modelMap.clear();
        return "redirect:/";
    }

}
