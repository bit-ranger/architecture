package com.rainyalley.architecture.service.impl;

import com.rainyalley.architecture.core.Page;
import com.rainyalley.architecture.service.config.ServiceConfig;
import com.rainyalley.architecture.dao.entity.UserDo;
import com.rainyalley.architecture.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {ServiceConfig.class})
@SpringBootApplication
@Transactional
public class UserServiceImplTest {


    @Resource
    private UserService userService;

    @Test
    @Rollback
    public void save() throws Exception {
        List<UserDo> userListBefore = userService.get(new UserDo(), new Page());
        UserDo user = new UserDo();
        user.setName("hello");
        user.setPassword("world");
        userService.save(user);

        List<UserDo> userListAfter = userService.get(new UserDo(), new Page());
        System.out.println(userListAfter);
        Assert.assertEquals(1, userListAfter.size() - userListBefore.size());
    }

    @Test
    @Rollback
    public void get() throws Exception {
        UserDo user = new UserDo();
        user.setName("hello");
        user.setPassword("world");
        userService.save(user);
        UserDo entity =  userService.get(user.getId());
        Assert.assertEquals(entity, user);
    }



}