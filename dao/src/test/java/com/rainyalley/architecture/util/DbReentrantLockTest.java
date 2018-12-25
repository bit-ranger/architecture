package com.rainyalley.architecture.util;

import com.rainyalley.architecture.mapper.ReentrantLockEntityMapper;
import com.rainyalley.architecture.util.sql.DbReentrantLock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DbReentrantLockTest implements InitializingBean {


    @Resource
    private TransactionTemplate transactionTemplate;


    @Autowired
    private ReentrantLockEntityMapper mapper;

    private String target = "test:01";

    private DbReentrantLock drl;


    private final static ThreadPoolExecutor es = new ThreadPoolExecutor(4, 4, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(4));


    @After
    public void after(){
        mapper.deleteByPrimaryKey(target);
    }

    @Before
    public void before(){
        mapper.deleteByPrimaryKey(target);
    }

    @Test

    public void hasLock() throws Exception {
        Assert.assertFalse(drl.hasLock());
    }

    @Test
    public void tryLock() throws Exception {
        Assert.assertFalse(drl.hasLock());
        Assert.assertTrue(drl.tryLock());
        Assert.assertTrue(drl.hasLock());
        Assert.assertTrue(drl.hasLock());
        Assert.assertTrue(drl.tryLock(10000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(drl.tryLock(10000, TimeUnit.MILLISECONDS));
        drl.unlock();
        Assert.assertTrue(drl.hasLock());
        drl.unlock();
        Assert.assertTrue(drl.hasLock());
        drl.unlock();
        Assert.assertFalse(drl.hasLock());
    }

    @Test
    public void lock() throws Exception {
        AtomicInteger lockNum = new AtomicInteger(0);
        int threadNum = 4;
        CountDownLatch latch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            es.execute(() -> {
                if(drl.tryLock()){
                    lockNum.incrementAndGet();
                }

                latch.countDown();
            });
        }

        latch.await();
        Assert.assertEquals(1, lockNum.get());
    }

    @Test
    public void unLock() throws Exception {
        Assert.assertFalse(drl.hasLock());
        Assert.assertTrue(drl.tryLock());
        Assert.assertTrue(drl.hasLock());
        Assert.assertTrue(drl.tryLock());
        drl.unlock();
        Assert.assertTrue(drl.hasLock());
        drl.unlock();
        Assert.assertFalse(drl.hasLock());


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        drl = new DbReentrantLock(target, 5000);
    }
}