package com.rainyalley.architecture.common.purchase.service.impl;

import com.rainyalley.architecture.common.purchase.service.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.util.Pool;

public class ShardedRedisLock implements Lock {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardedRedisLock.class);

    private Pool<? extends ShardedJedis> pool;

    public ShardedRedisLock(Pool<? extends ShardedJedis> pool){
        this.pool = pool;
    }

    /**
     * 检查锁
     * @param entityId
     * @param owner
     * @return
     */
    @Override
    public boolean hasLock(final String entityId, final String owner){
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            String value = client.get(entityId);
            return owner.equals(value);
        } catch (Exception e) {
            LOGGER.error("hasLock failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    /**
     * 尝试上锁
     * @param entityId
     * @param owner
     */
    @Override
    public boolean tryLock(final String entityId, final String owner){
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            Long status = client.setnx(entityId, owner);
            client.expire(entityId, 10);
            return Long.valueOf(1).equals(status);
        } catch (Exception e) {
            LOGGER.error("tryLock failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    /**
     * 自旋上锁
     * @param entityId
     * @param owner
     */
    @Override
    public boolean lock(final String entityId, final String owner){
        ShardedJedis client = null;
        try {

            client = pool.getResource();
            for(int i=0; i<30; i++){
                Long status = client.setnx(entityId, owner);
                if(Long.valueOf(1).equals(status)){
                    client.expire(entityId, 10);
                    return true;
                }
                Thread.sleep(100);
            }
            return false;
        } catch (Exception e) {
            LOGGER.error("lock failed:" + entityId, e);
            if(client != null && entityId != null){
                try {
                    client.del(entityId);
                } catch (Exception e1) {
                    return false;
                }
            }
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    /**
     * 开锁
     * @param entityId
     * @param owner
     */
    @Override
    public boolean unLock(final String entityId, final String owner){
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            String value = client.get(entityId);
            if(!owner.equals(value)){
                return true;
            }
            client.del(entityId);
            return true;
        } catch (Exception e) {
            LOGGER.error("unLock failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }
}
