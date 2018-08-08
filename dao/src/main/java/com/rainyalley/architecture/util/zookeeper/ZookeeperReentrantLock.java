package com.rainyalley.architecture.util.zookeeper;

import com.rainyalley.architecture.util.Lock;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author bin.zhang
 */
public class ZookeeperReentrantLock implements Lock {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperReentrantLock.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final String DEFAULT_DELIMITER = "/";

    private static final List<ACL> DEFAULT_ACL_LIST = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone")));

    private static final Stat DEFAULT_STAT = new Stat();

    private String delimiter = DEFAULT_DELIMITER;

    private List<ACL> aclList = DEFAULT_ACL_LIST;

    private Charset charset = DEFAULT_CHARSET;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private Stat stat = DEFAULT_STAT;

    private String lockNodeName;

    private String basePath;

    private ZooKeeper zk;

    private LockData lockData;


    public ZookeeperReentrantLock(String basePath, String lockNodeName, ZooKeeper zk) {
        if(basePath.substring(basePath.length()-1).equals(delimiter)){
            throw new IllegalArgumentException("basePath can not end with delimiter");
        }
        if(lockNodeName.contains(delimiter)){
            throw new IllegalArgumentException("lockNodeName can not contains delimiter");
        }
        if(zk == null){
            throw new IllegalArgumentException("zk can not be null");
        }
        this.zk = zk;
        this.basePath = basePath;
        this.lockNodeName = lockNodeName;
    }

    @Override
    public boolean hasLock() {
        boolean locked = lockData != null && lockData.getLockCount().get() > 0;
        if(!locked){
            return false;
        }
        return lockData.getCurrentThread().equals(Thread.currentThread());
    }

    @Override
    public boolean tryLock() {
        return tryLockInternal(false, -1);
    }

    @Override
    public boolean tryLock(long waitMs) {
        if(waitMs > 0){
            return tryLockInternal(true, waitMs);
        } else {
            return tryLockInternal(false, -1);
        }

    }

    @Override
    public boolean lock() {
        return tryLockInternal(true, -1);
    }

    private boolean tryLockInternal(boolean ifWait, long waitMs){
        try {
            if (hasLock()) {
                //原子递增一个当前值，记录重入次数，后面锁释放会用到
                lockData.getLockCount().incrementAndGet();
                return true;
            }
            //尝试连接zookeeper获取锁
            String lockedNodeName = this.attemptLock(ifWait, waitMs, serialize(new LockData(Thread.currentThread(), lockNodeName)));
            if (lockedNodeName != null) {
                //创建可重入锁数据，用于记录当前线程重入次数
                lockData = new LockData(Thread.currentThread(), lockedNodeName);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("tryLockInternal error",e);
            return false;
        }
    }

    @Override
    public boolean unLock() {
        try {
            if(!hasLock()){
                throw new IllegalMonitorStateException("is not locked");
            }

            if(lockData.getLockCount().get() > 1){
                lockData.getLockCount().decrementAndGet();
                return true;
            } else {
                return deleteNode(lockData.getLockedNodeName());
            }
        } catch (Exception e){
            logger.error("unLock error", e);
            return false;
        }
    }


    private String attemptLock(boolean ifWait, long waitTimeMs, byte[] lockNodeBytes) throws Exception {
        //获取当前时间戳
        final long startMillis = System.currentTimeMillis();
        int retryCount = 0;

        String currentNode = null;
        boolean hasTheLock = false;
        boolean isDone = false;
        //自旋锁，循环获取锁
        while (!isDone) {
            //若无异常，则只循环一次
            isDone = true;
            try {
                //在锁节点下创建临时且有序的子节点，例如:_c_008c1b07-d577-4e5f-8699-8f0f98a013b4-lock-000000001
                String currentPath = zk.create(
                        basePath + delimiter + lockNodeName,
                        lockNodeBytes,
                        aclList,
                        CreateMode.EPHEMERAL_SEQUENTIAL);
                String[] pathArr = currentPath.split(delimiter);
                currentNode = pathArr[pathArr.length-1];

                if(ifWait){
                    //如果当前子节点序号最小，获得锁则直接返回，否则阻塞等待前一个子节点删除通知(release释放锁)
                    hasTheLock = internalLockLoop(startMillis, waitTimeMs, currentNode);
                } else{
                    String previous = getPreviousSequenceNode(currentNode);
                    hasTheLock = previous == null;
                }
            } catch (KeeperException.NoNodeException e) {
                logger.warn("attemptLock error", e);
                if(retryCount < 1){
                    retryCount++;
                    isDone = false;
                } else {
                    isDone = true;
                }
            }
        }
        //如果获取锁则返回当前锁子节点路径
        if (hasTheLock) {
            if(logger.isDebugEnabled()){
                logger.debug(String.format("attemptLock success %s, %s, %s", currentNode, ifWait, waitTimeMs));
            }
            return currentNode;
        } else {
            if(logger.isDebugEnabled()){
                logger.debug(String.format("attemptLock failure %s, %s, %s", currentNode, ifWait, waitTimeMs));
            }
            deleteNode(currentNode);
            return null;
        }
    }

    private boolean internalLockLoop(long startMillis, long millisToWait, String currNodeName) throws Exception {
        boolean haveTheLock = false;
        while (!haveTheLock){
            //获取所有子节点集合
            String previous = getPreviousSequenceNode(currNodeName);
            //判断当前子节点是否为最小子节点
            //如果是最小节点则获取锁
            if (previous == null) {
                if(logger.isDebugEnabled()){
                    logger.debug(String.format("previous not exist %s", currNodeName));
                }
                haveTheLock = true;
            } else {
                if(logger.isDebugEnabled()){
                    logger.debug(String.format("previous exists %s", currNodeName));
                }
                lock.lock();
                try {
                    // 这里使用getData()接口而不是checkExists()是因为，
                    // 如果前一个子节点已经被删除了那么会抛出异常而且不会设置事件监听器，
                    // 而checkExists虽然也可以获取到节点是否存在的信息但是同时设置了监听器，
                    // 这个监听器其实永远不会触发，对于Zookeeper来说属于资源泄露
                    byte[] data = zk.getData(basePath + delimiter + previous, watcher, stat);
                    if(logger.isDebugEnabled()){
                        logger.debug(String.format("getData %s, %s, %s", currNodeName, previous, new String(data, charset)));
                    }
                    if (millisToWait > 0) {
                        //如果设置了获取锁等待时间
                        millisToWait -= (System.currentTimeMillis() - startMillis);
                        startMillis = System.currentTimeMillis();
                        if (millisToWait <= 0) {
                            // 超时
                            break;
                        } else {
                            // 等待超时时间
                            if(logger.isDebugEnabled()){
                                logger.debug(String.format("await(%s) before %s", millisToWait, currNodeName));
                            }
                            condition.await(millisToWait, TimeUnit.MILLISECONDS);
                            if(logger.isDebugEnabled()){
                                logger.debug(String.format("await(%s) after  %s", millisToWait, currNodeName));
                            }
                        }
                    } else {
                        //一直等待
                        if(logger.isDebugEnabled()){
                            logger.debug(String.format("await before %s", currNodeName));
                        }
                        condition.await();
                        if(logger.isDebugEnabled()){
                            logger.debug(String.format("await after  %s", currNodeName));
                        }
                    }
                } catch (KeeperException.NoNodeException e) {
                    // 如果前一个子节点已经被删除则只需要自旋获取一次即可
                    if(logger.isDebugEnabled()){
                        logger.debug(String.format("NoNodeException of %s", previous));
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        return haveTheLock;
    }

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            lock.lock();
            try {
                if(logger.isDebugEnabled()){
                    logger.debug(String.format("signalAll by %s", event));
                }
                condition.signalAll();
            } finally {
                lock.unlock();
            }

        }
    };

    private List<String> getSortedChildren(String parentPath) {
        List<String> children = null;
        try {
            children = zk.getChildren(parentPath, true);
        } catch (Exception e) {
            logger.error("getSortedChildren error", e);
            return Collections.emptyList();
        }
        Collections.sort(children);
        return children;
    }

    private String getPreviousSequenceNode(String currentNodeName){
        List<String> children = getSortedChildren(basePath);
        int idx = children.indexOf(currentNodeName);
        if(idx < 0){
            throw new IllegalArgumentException(String.format("no previous node of %s in %s", currentNodeName, children));
        }
        if(idx == 0){
            return null;
        } else {
            return children.get(idx - 1);
        }
    }

    private boolean deleteNode(String nodeName){
        try {
            zk.delete(basePath + delimiter + nodeName, -1);
            return true;
        } catch (Exception e) {
            logger.error("deleteNode error", e);
            return false;
        }
    }

    private byte[] serialize(Object object){
        return object.toString().getBytes(charset);
    }

    private static class LockData {

        private static String MACHINE_ID;

        static {
            try {
                MACHINE_ID = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                MACHINE_ID = UUID.randomUUID().toString();
            }
        }

        private String machineId = MACHINE_ID;

        private Thread currentThread;

        private String lockedNodeName;

        private AtomicInteger lockCount = new AtomicInteger(1);

        public LockData(Thread currentThread, String lockedNodeName) {
            this.currentThread = currentThread;
            this.lockedNodeName = lockedNodeName;
        }



        public Thread getCurrentThread() {
            return currentThread;
        }


        public AtomicInteger getLockCount() {
            return lockCount;
        }

        public String getLockedNodeName() {
            return lockedNodeName;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"@class\":\"com.rainyalley.architecture.util.zookeeper.ZookeeperReentrantLock.LockData\",");
            sb.append("\"@super\":\"").append(super.toString()).append("\",");
            sb.append("\"machineId\":\"")
                    .append(machineId)
                    .append("\"");
            sb.append(",\"currentThread\":\"")
                    .append(currentThread)
                    .append("\"");
            sb.append(",\"lockedNodeName\":\"")
                    .append(lockedNodeName)
                    .append("\"");
            sb.append(",\"lockCount\":\"")
                    .append(lockCount)
                    .append("\"");
            sb.append("}");
            return sb.toString();
        }
    }
}
