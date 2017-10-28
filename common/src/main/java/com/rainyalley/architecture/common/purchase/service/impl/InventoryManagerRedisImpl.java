package com.rainyalley.architecture.common.purchase.service.impl;

import com.rainyalley.architecture.common.purchase.service.InventoryLoader;
import com.rainyalley.architecture.common.purchase.service.InventoryManager;
import com.rainyalley.architecture.common.purchase.service.model.entity.OccupyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InventoryManagerRedisImpl  implements InventoryManager {

    private ExecutorService executorService =  Executors.newCachedThreadPool();

    private ShardedJedisPool pool;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryManagerRedisImpl.class);
	
    private String redisKeyPrefix;

    private InventoryLoader loader;

    private static final long TIME_OUT = 3*24*60*60L;

    public InventoryManagerRedisImpl(String redisKeyPrefix, InventoryLoader loader){
        this.redisKeyPrefix = redisKeyPrefix;
        this.loader = loader;
    }

    /**
     *
     * @param entityId 商品ID
     * @param occupyNum 抢占数量
     * @return
     */
    @Override
    public OccupyResult occupy(final String entityId, final long occupyNum){
        if(occupyNum < 1){
            throw new IllegalArgumentException();
        }

        long inventory = get(entityId);

        if(inventory < occupyNum){
            return new OccupyResult(entityId, occupyNum, 0, OccupyResult.FailureCause.UNDER_STOCK);
        }

        final String inventoryKey = inventoryKey(entityId);

        ShardedJedis client = null;
        try {
            client = pool.getResource();
            //设置库存
            inventory = client.decrBy(inventoryKey, occupyNum);


            if(inventory == 0){
                bgStore(entityId);
            } else if(inventory < 0) {
                //表示第一个超出库存的线程
                if(-occupyNum < inventory){
                    bgStore(entityId);
                }
                return new OccupyResult(entityId, occupyNum, 0, OccupyResult.FailureCause.UNDER_STOCK);
            }

            return new OccupyResult(entityId, occupyNum, inventory);
        } catch (Exception e) {
            LOGGER.error("occupy failed:" + entityId, e);
            return new OccupyResult(entityId, occupyNum, 0, OccupyResult.FailureCause.UNKNOWN_ERROR);
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    @Override
    public boolean release(String entityId, long releaseNum) {
        if(releaseNum < 1){
            return true;
        }

        final String inventoryKey = inventoryKey(entityId);
        ShardedJedis client = null;
        String lockOwner = "release";
        try {
            client = pool.getResource();
            if(!hasLock(entityId, lockOwner)){
                boolean locked = lock(entityId, lockOwner);
                if(!locked){
                    return false;
                }
            }

            client.decrBy(inventoryKey, releaseNum);
            return true;
        } catch (Exception e) {
            LOGGER.error("store failed:" + entityId, e);
            return false;
        } finally {
            unLock(entityId, lockOwner);
            if(client != null){
                client.close();
            }
        }
    }

    private void bgStore(final String entityId) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                store(entityId);
            }
        });
    }

    /**
     * 检查锁
     * @param entityId
     * @param owner
     * @return
     */
    private boolean hasLock(final String entityId, final String owner){
        final String inventoryLock = inventoryLockKey(entityId);
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            String value = client.get(inventoryLock);
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
    private boolean tryLock(final String entityId, final String owner){
        final String inventoryLock = inventoryLockKey(entityId);
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            Long status = client.setnx(inventoryLock, owner);
            client.expire(inventoryLock, 10);
            return Long.valueOf(1).equals(status);
        } catch (Exception e) {
            LOGGER.error("lock failed:" + entityId, e);
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
    private boolean lock(final String entityId, final String owner){
        final String inventoryLock = inventoryLockKey(entityId);
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            for(;;){
                Long status = client.setnx(inventoryLock, owner);
                if(Long.valueOf(1).equals(status)){
                    break;
                }
                Thread.sleep(100);
            }
            client.expire(inventoryLock, 10);
            return true;
        } catch (Exception e) {
            LOGGER.error("lock failed:" + entityId, e);
            if(client != null){
                try {
                    client.del(inventoryLock);
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
    private void unLock(final String entityId, final String owner){
        final String inventoryLock = inventoryLockKey(entityId);
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            String value = client.get(inventoryLock);
            if(!owner.equals(value)){
                return;
            }
            client.del(inventoryLock);
        } catch (Exception e) {
            LOGGER.error("unLock failed:" + entityId, e);
        } finally {
            if(client != null){
                client.close();
            }
        }
    }



    @Override
    public void store(final String entityId){
        final String inventoryKey = inventoryKey(entityId);
        ShardedJedis client = null;
        String lockOwner = "store";
        try {
            client = pool.getResource();
            if(!hasLock(entityId, lockOwner)){
                boolean locked = lock(entityId, lockOwner);
                if(!locked){
                    return;
                }
            }
            long loadedInventory = loader.load(entityId);
            client.set(inventoryKey, String.valueOf(loadedInventory));
        } catch (Exception e) {
            LOGGER.error("store failed:" + entityId, e);
        } finally {
            unLock(entityId, lockOwner);
            if(client != null){
                client.close();
            }
        }
    }

    @Override
    public long get(String entityId) {
        final String inventoryKey = inventoryKey(entityId);
        ShardedJedis client = null;

        try {
            client = pool.getResource();
            //设置库存
            String inventory = client.get(inventoryKey);
            if(inventory == null){
                return 0;
            }

            long inve = Long.valueOf(inventory);
            if(inve < 0){
                return 0;
            }

            return inve;
        } catch (Exception e) {
            LOGGER.error("get failed:" + entityId, e);
        } finally {
            if(client != null){
                client.close();
            }
        }
        return 0;
    }

    /**
     * 构造redis库存的key
     * @param entityId
     * @return
     */
    private String inventoryKey(String entityId){
        return redisKeyPrefix + ":" + entityId;
    }

    private String inventoryLockKey(String entityId){
        return redisKeyPrefix + ":lock:" + entityId;
    }


    public void setPool(ShardedJedisPool pool) {
        this.pool = pool;
    }
}
