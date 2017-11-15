package com.rainyalley.architecture.service.user.service.impl;

import com.rainyalley.architecture.service.ServiceConfiguration;
import com.rainyalley.architecture.service.user.model.entity.User;
import com.rainyalley.architecture.service.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {ServiceConfiguration.class})
public class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Test
    public void get() throws Exception {
        userService.get(new User());
    }

}