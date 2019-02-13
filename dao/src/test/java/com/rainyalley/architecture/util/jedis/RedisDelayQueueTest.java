package com.rainyalley.architecture.util.jedis;

import com.rainyalley.architecture.config.DaoConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DaoConfig.class})
public class RedisDelayQueueTest implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(RedisDelayQueueTest.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private RedisDelayQueue redisDelayQueue;

    @Before
    public void before(){
        redisDelayQueue.clear();
    }

    @After
    public void after(){
        redisDelayQueue.clear();
    }

    @Test
    public void size() {
        Assert.assertEquals(redisDelayQueue.size(), 0);
    }

    @Test
    public void add() {
        Job job = new Job("testTopic", 3, "textBody");
        redisDelayQueue.add(job);
        Assert.assertEquals(1, redisDelayQueue.size());
    }

    @Test
    public void poll() {
        redisDelayQueue.destroy();
        Job job = new Job("testTopic", 0, "textBody");
        redisDelayQueue.add(job);
        logger.debug("add");
        redisDelayQueue.runTask();
        logger.debug("poll");
        Job jobPoll = redisDelayQueue.poll();
        Assert.assertEquals(job, jobPoll);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        redisDelayQueue = new RedisDelayQueue(
                stringRedisTemplate,
                "RedisDelayQueueTest:",
                1000, 1000, TimeUnit.HOURS);
    }
}