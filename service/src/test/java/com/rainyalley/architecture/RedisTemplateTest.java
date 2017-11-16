package com.rainyalley.architecture;

import com.rainyalley.architecture.service.user.model.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {RedisTemplateTest.class})
@Transactional
public class RedisTemplateTest {


    @Autowired
    private RedisTemplate<String,User> userRedisTemplate;

    @Test
    public void redis(){
        // 保存字符串
        User user = new User();
        userRedisTemplate.opsForValue().set("user", user, 60, TimeUnit.MINUTES);
        Assert.assertEquals(user, userRedisTemplate.opsForValue().get("user"));
    }
}
