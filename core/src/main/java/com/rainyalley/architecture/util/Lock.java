package com.rainyalley.architecture.util;

/**
 * @author bin.zhang
 */
public interface Lock {

    /**
     * @return true 当前线程占有锁，false 当前线程不占有锁
     */
    boolean hasLock();

    /**
     * 尝试获取锁
     * @return
     */
    boolean tryLock();

    /**
     * 阻塞获取锁
     * @param waitMS 等待获取锁的时长，毫秒
     * @return
     */
    boolean tryLock(long waitMS);

    /**
     * 阻塞获取锁 永久等待
     * @return
     */
    boolean lock();

    /**
     * 解锁
     * @return true 解锁成功， false 锁不存在
     * @throws IllegalMonitorStateException 当前线程不拥有锁
     */
    boolean unLock();
}
