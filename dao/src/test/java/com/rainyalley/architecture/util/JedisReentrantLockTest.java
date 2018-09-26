package com.rainyalley.architecture.util;

import com.rainyalley.architecture.config.DaoConfig;
import com.rainyalley.architecture.util.jedis.JedisReentrantLock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DaoConfig.class})
public class JedisReentrantLockTest implements InitializingBean {

    @Autowired
    private StringRedisTemplate redis;


    private String lockKey = "lock:test:01";

    private JedisReentrantLock jrl;


    private final static ThreadPoolExecutor es = new ThreadPoolExecutor(4, 4, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(4));


    @After
    public void after(){
        redis.delete(lockKey);
    }

    @Test
    public void hasLock() throws Exception {
        redis.delete(lockKey);
        Assert.assertFalse(jrl.hasLock());
    }

    @Test
    public void tryLock() throws Exception {
        redis.delete(lockKey);
        Assert.assertFalse(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock());
        Assert.assertTrue(jrl.hasLock());
        Thread.sleep(5000);
        Assert.assertFalse(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock(10000));
        Assert.assertTrue(jrl.tryLock(10000));
        Assert.assertTrue(jrl.unLock());
        Assert.assertTrue(jrl.hasLock());
        Assert.assertTrue(jrl.unLock());
        Assert.assertFalse(jrl.hasLock());

    }

    @Test
    public void lock() throws Exception {
        redis.delete(lockKey);
        AtomicInteger lockNum = new AtomicInteger(0);
        int threadNum = 4;
        CountDownLatch latch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    if(jrl.tryLock()){
                        lockNum.incrementAndGet();
                    }

                    latch.countDown();
                }
            });
        }

        latch.await();
        Assert.assertEquals(1, lockNum.get());
    }

    @Test
    public void unLock() throws Exception {
        redis.delete(lockKey);
        Assert.assertFalse(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock());
        Assert.assertTrue(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock());
        Assert.assertTrue(jrl.unLock());
        Assert.assertTrue(jrl.hasLock());
        Assert.assertTrue(jrl.unLock());
        Assert.assertFalse(jrl.hasLock());


    }

    @Override
    public void afterPropertiesSet() throws Exception {

        jrl = new JedisReentrantLock(lockKey, redis);
        jrl.setLockMs(5000);
    }
}