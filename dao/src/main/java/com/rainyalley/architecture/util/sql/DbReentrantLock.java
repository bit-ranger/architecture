package com.rainyalley.architecture.util.sql;

import com.rainyalley.architecture.entity.ReentrantLockEntity;
import com.rainyalley.architecture.entity.ReentrantLockEntityExample;
import com.rainyalley.architecture.mapper.ReentrantLockEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * @author bin.zhang
 */
public class DbReentrantLock implements Lock {

    private static Logger logger = LoggerFactory.getLogger(DbReentrantLock.class);

    /**
     * 全局唯一ID，用于与其他机器区分开来
     */
    private static final UUID MACHINE_ID = UUID.randomUUID();


    /**
     * redis锁的key
     */
    private String target;

    /**
     *  持有锁的时长, 毫秒
     */
    private long lockKeepMs;


    private static ReentrantLockEntityMapper MAPPER;


    public static void init(ReentrantLockEntityMapper mapper, ScheduledThreadPoolExecutor executor){
        DbReentrantLock.MAPPER = mapper;
        executor.scheduleWithFixedDelay(() -> {
            ReentrantLockEntityExample example = new ReentrantLockEntityExample();
            example.createCriteria()
                    .andExpireTimeLessThan(new Date());
            int n = mapper.deleteByExample(example);
            if(n > 0){
                logger.warn(String.format("locks deleted %s", n));
            } else {
                if(logger.isDebugEnabled()){
                    logger.debug(String.format("locks deleted %s", n));
                }
            }
        }, 30, 60, TimeUnit.SECONDS);
    }


    /**
     * 随机数生成器
     */
    private Random random = new Random();


    public DbReentrantLock(String target) {
        this(target, 30000);
    }

    public DbReentrantLock(String target, long lockKeepMs) {
        this.target = target;
        this.lockKeepMs = lockKeepMs;
    }



    private String currentAsker(){
        return MACHINE_ID + ":" + String.valueOf(Thread.currentThread().getId());
    }

    private boolean hasLock(ReentrantLockEntity lv){
        return lv != null &&currentAsker().equals(lv.getLockKeeper()) && lv.getEntrantCount() > 0;
    }


    public boolean hasLock() {
        try {

            ReentrantLockEntity lockVal = MAPPER.selectByPrimaryKey(target);
            return lockVal != null && hasLock(lockVal);
        } catch (Exception e) {
            logger.error("hasLock",e);
            return false;
        }
    }

    @Override
    public boolean tryLock() {

        try {
            ReentrantLockEntity lockVal = MAPPER.selectByPrimaryKey(target);
            if(lockVal == null){
                ReentrantLockEntity lv = new ReentrantLockEntity();
                lv.setTarget(target);
                lv.setAcquireTime(new Date(System.currentTimeMillis()));
                lv.setExpireTime(new Date(System.currentTimeMillis() + lockKeepMs));
                lv.setEntrantCount(1);
                lv.setLockKeeper(currentAsker());

                int inserted = MAPPER.insert(lv);

                if(inserted > 0){
                    if(logger.isDebugEnabled()){
                        logger.debug("tryLock success   >>> {}", lv);
                    }
                    return true;
                } else {
                    if(logger.isDebugEnabled()){
                        logger.debug("tryLock failure   >>> {}", lv);
                    }
                    return false;
                }

            }

            //拥有锁
            if(hasLock(lockVal)){
                String lockValBefore = lockVal.toString();

                lockVal.setAcquireTime(new Date(System.currentTimeMillis()));
                lockVal.setExpireTime(new Date(System.currentTimeMillis() + lockKeepMs));
                lockVal.setEntrantCount(lockVal.getEntrantCount() + 1);
                MAPPER.updateByPrimaryKey(lockVal);
                if(logger.isDebugEnabled()){
                    logger.debug("tryLock reentrant >>> {} -> {}", lockValBefore, lockVal);
                }
                return true;
            } else {
                //当前线程不拥有锁
                //且锁已被其他线程占有
                //无法获得锁
                //必须等待锁被释放后才能获得锁
                //释放锁的两种方式，1：拥有者主动释放，2：锁超时被自动清除
                return false;
            }

        } catch (Exception e) {
            logger.error("tryLock", e);
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(unit.toMillis(time));
    }


    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    private boolean tryLock(long waitMS) {
        long lockBeginMs = System.currentTimeMillis();
        for (;;) {
            boolean trySuccess = tryLock();
            if(trySuccess){
                return true;
            }

            if(System.currentTimeMillis() - lockBeginMs >= waitMS){
                return false;
            }

            //100毫秒~150毫秒之间随机睡眠，错开并发
            try {
                Thread.sleep(random.nextInt(50) + 100);
            } catch (InterruptedException e) {
                //
            }
        }
    }

    @Override
    public void lock() {
        for (;;) {
            boolean trySuccess = tryLock();
            if(trySuccess){
                return;
            }
            //100毫秒~150毫秒之间随机睡眠，错开并发
            try {
                Thread.sleep(random.nextInt(50) + 100);
            } catch (InterruptedException e) {
                //
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        try {
            ReentrantLockEntity lockVal = MAPPER.selectByPrimaryKey(target);
            if(lockVal == null){
                if(logger.isDebugEnabled()){
                    logger.debug("unLock  miss      >>> {} -> {}", target, currentAsker());
                }
                return;
            }

            //拥有锁
            if(hasLock(lockVal)){
                String lockValBefore = lockVal.toString();
                if(lockVal.getEntrantCount() > 1){
                    lockVal.setAcquireTime(new Date(System.currentTimeMillis()));
                    lockVal.setExpireTime(new Date(System.currentTimeMillis() + lockKeepMs));
                    lockVal.setEntrantCount(lockVal.getEntrantCount() - 1);
                    MAPPER.updateByPrimaryKey(lockVal);
                    if(logger.isDebugEnabled()){
                        logger.debug("unLock  reentrant >>> {} -> {}", lockValBefore, lockVal);
                    }
                } else {
                    int del = MAPPER.deleteByPrimaryKey(target);
                    if(logger.isDebugEnabled()){
                        logger.debug("unLock  delete    >>> {} -> {}", target, del, lockValBefore);
                    }
                }
            } else {
                //当前线程不拥有锁
                throw new IllegalMonitorStateException();
            }
        } catch (Exception e) {
            logger.error("unLock",e);
            throw new IllegalStateException("failed to unlock");
        }
    }
}
