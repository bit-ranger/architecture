package com.rainyalley.architecture.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.validation.constraints.NotNull;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * @author bin.zhang
 */
public class JedisReentrantLock implements Lock {

    /**
     * 全局唯一ID，用于与其他机器区分开来
     */
    private static final UUID MACHINE_ID = UUID.randomUUID();

    private static Logger logger = LoggerFactory.getLogger(JedisReentrantLock.class);

    /**
     * redis锁的key
     */
    private String lockKey;

    /**
     *  锁定时长
     */
    private long lockMs = 20000;

    /**
     * redis客户端，需要自行保证线程安全
     */
    private RedisOperations<String,String> jedis;


    /**
     * 随机数生成器
     */
    private Random random = new Random();

    /**
     * json转换器
     */
    private ObjectMapper om = new ObjectMapper();

    public JedisReentrantLock(String lockKey, RedisOperations<String,String> jedis) {
        this.lockKey = lockKey;
        this.jedis = jedis;
    }


    private String currentAsker(){
        return MACHINE_ID + ":" + String.valueOf(Thread.currentThread().getId());
    }

    private boolean hasLock(LockData lv){
        if (currentAsker().equals(lv.lockOwner)) {
            return true;
        }
        return false;
    }


    public boolean hasLock() {
        try {
            String lockValTxt = jedis.opsForValue().get(lockKey);
            if(StringUtils.isBlank(lockValTxt)){
                return false;
            }
            LockData lockVal = om.readValue(lockValTxt, LockData.class);

            return hasLock(lockVal);
        } catch (Exception e) {
            logger.error("hasLock",e);
            return false;
        }
    }

    @Override
    public boolean tryLock() {

        try {
            String lockValTxt = jedis.opsForValue().get(lockKey);
            if(StringUtils.isBlank(lockValTxt)){
                LockData lv = new LockData(System.currentTimeMillis(), lockMs, currentAsker(), 1);
                String lvTxt = om.writeValueAsString(lv);
                Boolean result = setIfAbsent(lockKey, lvTxt, lockMs);
                if(logger.isDebugEnabled()){
                    logger.debug("tryLock acquire   >>> {} -> {} -> {}", lockKey, result, lvTxt);
                }
                return result;
            }

            LockData lockVal = om.readValue(lockValTxt, LockData.class);
            //拥有锁
            if(hasLock(lockVal)){
                lockVal.setAcquireMs(System.currentTimeMillis());
                lockVal.setCount(lockVal.getCount() + 1);
                String lvTxt = om.writeValueAsString(lockVal);
                Boolean result = setIfPersist(lockKey, lvTxt, lockMs);
                if(logger.isDebugEnabled()){
                    logger.debug("tryLock reentrant >>> {} -> {} -> {} -> {}", lockKey, result, lockValTxt, lvTxt);
                }
                return result;
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
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return tryLock(unit.toMillis(time));
    }


    @NotNull
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
            String lockValTxt = jedis.opsForValue().get(lockKey);
            if(StringUtils.isBlank(lockValTxt)){
                if(logger.isDebugEnabled()){
                    logger.debug("unLock  miss      >>> {} -> {}", lockKey, currentAsker());
                }
                return;
            }

            LockData lockVal = om.readValue(lockValTxt, LockData.class);
            //拥有锁
            if(hasLock(lockVal)){
                if(lockVal.getCount() > 1){
                    lockVal.setAcquireMs(System.currentTimeMillis());
                    lockVal.setCount(lockVal.getCount() - 1);
                    String lvTxt = om.writeValueAsString(lockVal);
                    Boolean result = setIfPersist(lockKey, lvTxt, lockVal.expireMs);
                    if(logger.isDebugEnabled()){
                        logger.debug("unLock  reentrant >>> {} -> {} -> {} -> {}", lockKey, result, lockValTxt, lvTxt);
                    }
                } else {
                    Boolean del = jedis.delete(lockKey);
                    if(logger.isDebugEnabled()){
                        logger.debug("unLock  delete    >>> {} -> {}", lockKey, del, lockValTxt);
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


    private Boolean setIfPersist(String key, String value, long expireMs){
        return set(key, value, expireMs, RedisStringCommands.SetOption.SET_IF_PRESENT);
    }

    private Boolean setIfAbsent(String key, String value, long expireMs){
        return set(key, value, expireMs, RedisStringCommands.SetOption.SET_IF_ABSENT);
    }

    private Boolean set(String key, String value, long expireMs, RedisStringCommands.SetOption option){
        return jedis.execute(
                (RedisCallback<Boolean>) connection -> {
                    RedisSerializer<String> keySeria = (RedisSerializer<String>)jedis.getKeySerializer();
                    RedisSerializer<String> valueSeria = (RedisSerializer<String>)jedis.getValueSerializer();
                    return connection.set(
                            keySeria.serialize(key),
                            valueSeria.serialize(value),
                            Expiration.milliseconds(expireMs),
                            option
                    );
                }
        );
    }



    private static class LockData {
        private long acquireMs;
        private long expireMs;
        private String lockOwner;
        private int count;

        public LockData() {
        }

        public LockData(long acquireMs, long expireMs, String lockOwner, int count) {
            this.acquireMs = acquireMs;
            this.expireMs = expireMs;
            this.lockOwner = lockOwner;
            this.count = count;
        }

        public long getAcquireMs() {
            return acquireMs;
        }

        public void setAcquireMs(long acquireMs) {
            this.acquireMs = acquireMs;
        }

        public long getExpireMs() {
            return expireMs;
        }

        public void setExpireMs(long expireMs) {
            this.expireMs = expireMs;
        }

        public String getLockOwner() {
            return lockOwner;
        }

        public void setLockOwner(String lockOwner) {
            this.lockOwner = lockOwner;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"@class\":\"com.rainyalley.architecture.util.jedis.JedisReentrantLock.LockData\",");
            sb.append("\"@super\":\"").append(super.toString()).append("\",");
            sb.append("\"acquireMs\":\"")
                    .append(acquireMs)
                    .append("\"");
            sb.append(",\"expireMs\":\"")
                    .append(expireMs)
                    .append("\"");
            sb.append(",\"lockOwner\":\"")
                    .append(lockOwner)
                    .append("\"");
            sb.append(",\"count\":\"")
                    .append(count)
                    .append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    public long getLockMs() {
        return lockMs;
    }

    public void setLockMs(long lockMs) {
        this.lockMs = lockMs;
    }
}
