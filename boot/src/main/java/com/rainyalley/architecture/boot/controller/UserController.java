package com.rainyalley.architecture.boot.controller;

import com.rainyalley.architecture.core.Page;
import com.rainyalley.architecture.model.entity.User;
import com.rainyalley.architecture.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/{id:[0-9]{1,9}}", method = RequestMethod.GET)
    public User get(@PathVariable("id") int id){
        User user = new User();
        user.setId(id);
        User entity = userService.get(user);
        return entity;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> list(){
        List<User> entityList = userService.get(new User(), new Page());
        return entityList;
    }
}
