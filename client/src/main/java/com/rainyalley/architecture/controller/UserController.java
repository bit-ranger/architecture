package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.client.UserClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {

    private UserClient userClient;

    @GetMapping(path = "{id}")
    @ResponseBody
    public String get(@PathVariable(name = "id") String id) {
        return userClient.get(id);
    }

    @Resource
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
}
