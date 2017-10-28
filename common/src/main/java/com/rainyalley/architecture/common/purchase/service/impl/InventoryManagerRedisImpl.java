package com.rainyalley.architecture.common.purchase.service.impl;

import com.rainyalley.architecture.common.purchase.service.InventoryLoader;
import com.rainyalley.architecture.common.purchase.service.InventoryManager;
import com.rainyalley.architecture.common.purchase.service.model.entity.OccupyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 共有3个操作会修改库存值
 * {@link #occupy(String, long)}
 * {@link #release(String, long)}
 * {@link #store(String)}
 *
 * occupy 与 release 都是通过原子增长实现，不会出现并发修改问题
 * occupy 与 store 必须控制使其不会发生并发修改，即当库存为0时才能调用store,否则可能数据错误
 * release 与 store 可能同时发生，需要考虑并发问题，通过竞争setNX模拟锁，实现串行化
 */
public class InventoryManagerRedisImpl  implements InventoryManager {

    private ScheduledExecutorService scheduledExecutorService =  Executors.newScheduledThreadPool(1);

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

            inventory = client.decrBy(inventoryKey, occupyNum);

            if(inventory < 0) {
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

        ShardedJedis client = null;
        try {


            final String inventoryKey = inventoryKey(entityId);
            client = pool.getResource();
            final Long inventory = _get(client, entityId);
            if(inventory == null){
                return false;
            } else if(inventory < 0){
                String lockOwner = "release";
                try {
                    if(!lock(entityId, lockOwner)){return false;}
                    final Long iven = _get(client, entityId);
                    if(iven == null){
                        return false;
                    } else if(iven < 0){
                        client.set(inventoryKey, String.valueOf(releaseNum));
                        return true;
                    } else {
                        client.incrBy(inventoryKey, releaseNum);
                        return true;
                    }
                } finally {
                    unLock(entityId, lockOwner);
                }
            } else {
                client.incrBy(inventoryKey, releaseNum);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("store failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    /**
     * 异步重置库存
     * @param entityId
     */
    private void asyncStore(final String entityId, final long seconds) {
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success = store(entityId);
                    if(!success){
                        asyncStore(entityId, seconds * 2);
                    }
                } catch (Exception e) {
                    asyncStore(entityId, seconds * 2);
                }

            }
        }, seconds, TimeUnit.SECONDS);
    }


    /**
     * 检查锁
     * @param entityId
     * @param owner
     * @return
     */
    private boolean hasLock(final String entityId, final String owner){
        ShardedJedis client = null;
        try {
            final String inventoryLock = inventoryLockKey(entityId);
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
        ShardedJedis client = null;
        try {
            final String inventoryLock = inventoryLockKey(entityId);
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
        ShardedJedis client = null;
        String inventoryLock = null;
        try {
            inventoryLock = inventoryLockKey(entityId);
            client = pool.getResource();
            for(int i=0; i<30; i++){
                Long status = client.setnx(inventoryLock, owner);
                if(Long.valueOf(1).equals(status)){
                    client.expire(inventoryLock, 10);
                    return true;
                }
                Thread.sleep(100);
            }
            return false;
        } catch (Exception e) {
            LOGGER.error("lock failed:" + entityId, e);
            if(client != null && inventoryLock != null){
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
    private boolean unLock(final String entityId, final String owner){
        final String inventoryLock = inventoryLockKey(entityId);
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            String value = client.get(inventoryLock);
            if(!owner.equals(value)){
                return true;
            }
            client.del(inventoryLock);
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



    @Override
    public boolean store(final String entityId){
        final String inventoryKey = inventoryKey(entityId);
        ShardedJedis client = null;
        String lockOwner = "store";
        try {
            client = pool.getResource();
            if(!hasLock(entityId, lockOwner)){
                boolean locked = lock(entityId, lockOwner);
                if(!locked){
                    return false;
                }
            }
            long loadedInventory = loader.load(entityId);
            client.set(inventoryKey, String.valueOf(loadedInventory));
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


    private Long _get(ShardedJedis client, String entityId){
        final String inventoryKey = inventoryKey(entityId);
        String inventory = client.get(inventoryKey);
        if(inventory == null){
            return null;
        }

        return Long.valueOf(inventory);
    }

    @Override
    public long get(String entityId) {

        ShardedJedis client = null;

        try {
            client = pool.getResource();
            Long inventory = _get(client, entityId);
            if(inventory == null){
                return 0;
            }

            if(inventory < 0){
                return 0;
            }

            return inventory;
        } catch (Exception e) {
            LOGGER.error("get failed:" + entityId, e);
            return 0;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    /**
     * 构造redis库存的key
     * @param entityId
     * @return
     */
    private String inventoryKey(String entityId){
        return redisKeyPrefix + "inventory:" + entityId;
    }

    private String inventoryLockKey(String entityId){
        return redisKeyPrefix + ":lock:" + entityId;
    }

    public void setPool(ShardedJedisPool pool) {
        this.pool = pool;
    }
}
