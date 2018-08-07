package com.rainyalley.architecture.util.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

public class ZookeeperReentrantLockTest {

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
    public void lock() {
        lock.lock(50000, -1);
        lock.unLock();
    }
}