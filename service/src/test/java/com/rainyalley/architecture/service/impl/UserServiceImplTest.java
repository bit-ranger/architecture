package com.rainyalley.architecture.service.impl;

import com.rainyalley.architecture.core.Page;
import com.rainyalley.architecture.service.ServiceConfig;
import com.rainyalley.architecture.service.UserService;
import com.rainyalley.architecture.service.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceConfig.class})
@Transactional
public class UserServiceImplTest {


    @Resource
    private UserService userService;

    @Test
    @Rollback
    public void save() throws Exception {
        List<User> userListBefore = userService.get(new User(), new Page());
        User user = new User();
        user.setName("hello");
        user.setPassword("world");
        userService.save(user);

        List<User> userListAfter = userService.get(new User(), new Page());
        System.out.println(userListAfter);
        Assert.assertEquals(1, userListAfter.size() - userListBefore.size());
    }

    @Test
    @Rollback
    public void get() throws Exception {
        User user = new User();
        user.setName("hello");
        user.setPassword("world");
        user = userService.save(user);
        User entity =  userService.get(user.getId());
        Assert.assertEquals(entity, user);
    }



}