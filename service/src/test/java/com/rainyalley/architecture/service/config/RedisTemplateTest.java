package com.rainyalley.architecture.service.config;

import com.rainyalley.architecture.dao.entity.UserDo;
import com.rainyalley.architecture.service.BootApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootApplication.class})
@Transactional
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate userRedisTemplate;

    @Test
    public void redis(){
        // 保存字符串
        UserDo user = new UserDo();
        userRedisTemplate.opsForValue().set("user", user.toString(), 60, TimeUnit.MINUTES);
        Assert.assertEquals(user.toString(), userRedisTemplate.opsForValue().get("user"));
    }
}
