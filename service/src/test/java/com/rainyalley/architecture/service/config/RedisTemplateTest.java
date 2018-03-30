package com.rainyalley.architecture.service.config;

import com.rainyalley.architecture.dao.entity.UserDo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
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
    private CacheManager cacheManager;


    @Autowired
    private RedisTemplate<String,UserDo> userRedisTemplate;

    @Test
    public void redis(){
        // 保存字符串
        UserDo user = new UserDo();
        userRedisTemplate.opsForValue().set("user", user, 60, TimeUnit.MINUTES);
        Assert.assertEquals(user, userRedisTemplate.opsForValue().get("user"));
    }
}
