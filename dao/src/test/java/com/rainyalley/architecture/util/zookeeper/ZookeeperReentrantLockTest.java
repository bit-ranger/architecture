package com.rainyalley.architecture.util.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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


            String basePath = "/test/ZookeeperReentrantLockTest";
            Arrays.stream(basePath.split("/")).reduce((p,n) -> {
                String cp = p+ "/" +n;
                try {
                    Stat stat = zk.exists(cp, false);
                    if(stat == null){
                        zk.create(cp, new byte[0], Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone"))), CreateMode.PERSISTENT);
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return cp;
            });

            List<String> children = zk.getChildren(basePath, false);
            for (String child : children) {
                zk.delete(basePath + "/" + child, -1);
            }
            lock = new ZookeeperReentrantLock(basePath, "testLock", zk);
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

                    if(lock.hasLock()){
                        lock.unLock();
                    }
                }
            });
        }

        latch.await();
        Assert.assertEquals(1, lockNum.get());
    }

    @Test
    public void tryLockWait() throws Exception{

        AtomicInteger lockNum = new AtomicInteger(0);
        int threadNum = 4;
        CountDownLatch latch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    if(lock.tryLock(5000)){
                        lockNum.incrementAndGet();
                    }

                    latch.countDown();
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lock.hasLock()){
                        lock.unLock();
                    }
                }
            });
        }

        latch.await();
        Assert.assertEquals(4, lockNum.get());
    }
}