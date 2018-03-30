package com.rainyalley.architecture.service.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JedisReentrantLockTest {

    private JedisCluster jedis = new JedisCluster(getHostAndPort());

    private String lockKey = "lock:test:01";

    private JedisReentrantLock jrl = new JedisReentrantLock(lockKey, jedis);

    private final static ThreadPoolExecutor es = new ThreadPoolExecutor(4, 4, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(4));


    private Set<HostAndPort> getHostAndPort(){
        Set<HostAndPort> hosts = new HashSet<HostAndPort>();
        String[] adds = "192.168.4.110:8340|192.168.4.112:8340|192.168.4.113:8340".split("\\|");
        for (int i = 0; i < adds.length; i++) {
            String[] params = adds[i].split("\\:");
            HostAndPort host = new HostAndPort(params[0], Integer.valueOf(params[1]));
            hosts.add(host);
        }
        return hosts;
    }


    @After
    public void after(){
        jedis.del(lockKey);
    }

    @Test
    public void hasLock() throws Exception {
        jedis.del(lockKey);
        Assert.assertFalse(jrl.hasLock());
    }

    @Test
    public void tryLock() throws Exception {
        jedis.del(lockKey);
        Assert.assertFalse(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock(5000));
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
        jedis.del(lockKey);
        AtomicInteger lockNum = new AtomicInteger(0);
        int threadNum = 4;
        CountDownLatch latch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    if(jrl.lock(3000, 5000)){
                        lockNum.incrementAndGet();
                    }

                    latch.countDown();
                }
            });
        }

        latch.await();
        Assert.assertEquals(2, lockNum.get());
    }

    @Test
    public void unLock() throws Exception {
        jedis.del(lockKey);
        Assert.assertFalse(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock(5000));
        Assert.assertTrue(jrl.hasLock());
        Assert.assertTrue(jrl.tryLock(5000));
        Assert.assertTrue(jrl.unLock());
        Assert.assertTrue(jrl.hasLock());
        Assert.assertTrue(jrl.unLock());
        Assert.assertFalse(jrl.hasLock());


    }

}