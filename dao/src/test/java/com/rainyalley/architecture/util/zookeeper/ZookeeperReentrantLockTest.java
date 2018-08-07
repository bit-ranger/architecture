package com.rainyalley.architecture.util.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ZookeeperReentrantLockTest {

    private final static ThreadPoolExecutor es = new ThreadPoolExecutor(4, 4, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(4));

    private ZookeeperReentrantLock lock;

    @Before
    public void before(){
        try {
            ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 30000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("----empty----");
                }
            });
//            zk.addAuthInfo("world", "anyone".getBytes(Charset.forName("UTF-8")));

//            String basePath = zk.create("/ZookeeperReentrantLockTest", new byte[0], Arrays.asList(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone"))), CreateMode.PERSISTENT);
            lock = new ZookeeperReentrantLock("/ZookeeperReentrantLockTest", "testLock", zk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tryLock() throws Exception{

        AtomicInteger lockNum = new AtomicInteger(0);
        int threadNum = 4;
        CountDownLatch latch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    if(lock.tryLock()){
                        lockNum.incrementAndGet();
                    }

                    latch.countDown();
                }
            });
        }

        latch.await();
        Assert.assertEquals(1, lockNum.get());
    }
}