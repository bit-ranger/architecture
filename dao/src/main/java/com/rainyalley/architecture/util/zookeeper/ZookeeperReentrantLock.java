package com.rainyalley.architecture.util.zookeeper;

import com.rainyalley.architecture.util.Lock;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ZookeeperReentrantLock implements Lock {

    private String lockNodeName;

    private String basePath;

    private ZooKeeper zk;

    private LockData lockData;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public ZookeeperReentrantLock(String basePath, String lockNodeName, ZooKeeper zk) {
        this.zk = zk;
        this.basePath = basePath;
        this.lockNodeName = lockNodeName;
    }

    @Override
    public boolean hasLock() {
        return false;
    }

    @Override
    public boolean tryLock(long lockMs) {
        return false;
    }

    @Override
    public boolean lock(long lockMs, long waitMS) {
        try {
        /*
         实现同一个线程可重入性，如果当前线程已经获得锁，
         则增加锁数据中lockCount的数量(重入次数)，直接返回成功
        */
            //获取当前线程
            //获取当前线程重入锁相关数据
            if (lockData != null) {
                //原子递增一个当前值，记录重入次数，后面锁释放会用到
                lockData.getLockCount().incrementAndGet();
                return true;
            }
            //尝试连接zookeeper获取锁
            String lockedNodeName = this.attemptLock(waitMS, new LockData(Thread.currentThread(), lockNodeName).toString().getBytes(Charset.forName("UTF-8")));
            if (lockedNodeName != null) {
                //创建可重入锁数据，用于记录当前线程重入次数
                lockData = new LockData(Thread.currentThread(), lockedNodeName);
                return true;
            }
            //获取锁超时或者zk通信异常返回失败
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unLock() {
        try {
            zk.delete(basePath + "/" + lockData.getLockedNodeName(), -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return true;
    }


    private String attemptLock(long waitTimeMs, byte[] lockNodeBytes) throws Exception {
        //获取当前时间戳
        final long startMillis = System.currentTimeMillis();
        //如果unit不为空(非阻塞锁)，把当前传入time转为毫秒
        //子节点标识
        int retryCount = 0;

        String currentNode = null;
        boolean hasTheLock = false;
        boolean isDone = false;
        //自旋锁，循环获取锁
        while (!isDone) {
            isDone = true;
            try {
                //在锁节点下创建临时且有序的子节点，例如:_c_008c1b07-d577-4e5f-8699-8f0f98a013b4-lock-000000001
                String currentPath = zk.create(basePath + "/" + lockNodeName, lockNodeBytes, Arrays.asList(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone"))), CreateMode.EPHEMERAL_SEQUENTIAL);
                String[] pathArr = currentPath.split("/");
                currentNode = pathArr[pathArr.length-1];
                //如果当前子节点序号最小，获得锁则直接返回，否则阻塞等待前一个子节点删除通知(release释放锁)
                hasTheLock = internalLockLoop(startMillis, waitTimeMs, currentNode);
            } catch (KeeperException.NoNodeException e) {
                isDone = false;
                //异常处理，如果找不到节点，这可能发生在session过期等时，因此，如果重试允许，只需重试一次即可
//                if (client.getZookeeperClient().getRetryPolicy().allowRetry(retryCount++, System.currentTimeMillis() - startMillis, RetryLoop.getDefaultRetrySleeper())) {
//                    isDone = false;
//                } else {
//                    throw e;
//                }
            }
        }
        //如果获取锁则返回当前锁子节点路径
        if (hasTheLock) {
            return currentNode;
        }

        return null;
    }

    private boolean internalLockLoop(long startMillis, long millisToWait, String currNodeName) throws Exception {
        boolean haveTheLock = false;
        boolean doDelete = false;
        try {
            while (!haveTheLock){
                //获取所有子节点集合
                String previous = getPreviousSequenceNode(currNodeName);
                //判断当前子节点是否为最小子节点
                //如果是最小节点则获取锁
                if (previous == null) {
                    haveTheLock = true;
                } else {
                    lock.lock();
                    try {
                        //这里使用getData()接口而不是checkExists()是因为，如果前一个子节点已经被删除了那么会抛出异常而且不会设置事件监听器，而checkExists虽然也可以获取到节点是否存在的信息但是同时设置了监听器，这个监听器其实永远不会触发，对于Zookeeper来说属于资源泄露
                        byte[] data = zk.getData(basePath + "/" + previous, watcher, new Stat());
                        System.out.println(new String(data, Charset.forName("UTF-8")));
                        if (millisToWait > 0) {
                            millisToWait -= (System.currentTimeMillis() - startMillis);
                            startMillis = System.currentTimeMillis();
                            //如果设置了获取锁等待时间
                            if (millisToWait <= 0) {
                                doDelete = true;    // 超时则删除子节点
                            } else {
                                //等待超时时间
                                condition.await(millisToWait, TimeUnit.MILLISECONDS);
                            }

                        } else {
                            //一直等待
                            condition.await();
                        }
                    } catch (KeeperException.NoNodeException e) {
                        e.printStackTrace();
                        // it has been deleted (i.e. lock released). Try to acquire again
                        // 如果前一个子节点已经被删除则deException，只需要自旋获取一次即可
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (Exception e) {
//            ThreadUtils.checkInterrupted(e);
            doDelete = true;
            throw e;
        } finally {
            if (doDelete) {
//                deleteOurPath(currNodeName);//获取锁超时则删除节点
            }
        }
        return haveTheLock;
    }

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            lock.lock();
            try {
                System.out.println("---signalAll---" + event);
                condition.signalAll();
            } finally {
                lock.unlock();
            }

        }
    };

    private List<String> getSortedChildren(String parentPath){
        List<String> children = null;
        try {
            children = zk.getChildren(parentPath, true);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Collections.sort(children);
        return children;
    }

    private String getPreviousSequenceNode(String currentNodeName){
        List<String> children = getSortedChildren(basePath);
        int idx = children.indexOf(currentNodeName);
        if(idx < 0){
            throw new IllegalArgumentException("no previous node of " + currentNodeName);
        }
        if(idx == 0){
            return null;
        } else {
            return children.get(idx - 1);
        }
    }

}
