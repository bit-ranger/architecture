package com.rainyalley.architecture.dao.util;

import org.apache.zookeeper.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ZookeeperTest implements Watcher{

    private Logger logger = LoggerFactory.getLogger(ZookeeperTest.class);

    @Override
    public void process(WatchedEvent event) {
        if(event.getState() == Watcher.Event.KeeperState.SyncConnected){
            logger.info(event.toString());
        }
    }


    @Test
    public void test() throws IOException, KeeperException, InterruptedException {
        String host = "127.0.0.1:2181";
        String groupName = "test";

        ZooKeeper zk = new ZooKeeper(host, 10000, this);
        try {
            String path = "/" + groupName;
            if(zk.exists(path, false) == null){
                zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            List<String> children =  zk.getChildren("/", false);

            Assert.assertTrue(children.contains(groupName));

            zk.delete(path, 0);
            children =  zk.getChildren("/", false);
            Assert.assertTrue(!children.contains(groupName));
        } catch (KeeperException e) {
            e.printStackTrace();
        } finally {
            zk.close();
        }

    }

}
