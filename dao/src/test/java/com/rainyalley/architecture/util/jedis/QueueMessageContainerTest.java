package com.rainyalley.architecture.util.jedis;

import com.rainyalley.architecture.config.DaoConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DaoConfig.class})
public class QueueMessageContainerTest implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(QueueMessageContainerTest.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private RedisDelayQueue redisDelayQueue;

    private QueueMessageContainer queueMessageContainer;

    private Job job = new Job("testTopic", 1, "textBody");

    @Test
    public void runTask() {
        redisDelayQueue.clear();
        redisDelayQueue.add(job);
        redisDelayQueue.runTask();
        queueMessageContainer.runTask();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisDelayQueue = new RedisDelayQueue(
                stringRedisTemplate,
                "RedisDelayQueueTest:",
                1000, 1000, TimeUnit.HOURS);
        Map<String, Observer> observerMap = new HashMap<>();
        observerMap.put("testTopic", new InternalObserver());
        queueMessageContainer = new QueueMessageContainer(redisDelayQueue, observerMap,
                1000, 1000, TimeUnit.HOURS);
    }

    private class  InternalObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            logger.info("delay message : " + arg);
            Assert.assertEquals(job, arg);
        }
    }
}