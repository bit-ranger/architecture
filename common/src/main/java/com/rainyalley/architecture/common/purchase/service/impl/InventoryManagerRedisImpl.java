package com.rainyalley.architecture.common.purchase.service.impl;

import com.rainyalley.architecture.common.purchase.service.InventoryLoader;
import com.rainyalley.architecture.common.purchase.service.InventoryManager;
import com.rainyalley.architecture.common.purchase.service.Lock;
import com.rainyalley.architecture.common.purchase.service.model.entity.OccupyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 共有3个操作会修改库存值
 * {@link #occupy(String, long)}
 * {@link #release(String, long)}
 * {@link #store(String)}
 *
 * occupy 与 release 都是通过原子增长实现，不会出现并发修改问题
 * occupy 与 store  不会发生并发修改，即当库存为0，且所有订单处于最终状态时才调用store
 * release 与 store 不会发生并发修改，所有订单处于最终状态时才调用store
 */
public class InventoryManagerRedisImpl  implements InventoryManager {

    private ShardedJedisPool pool;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryManagerRedisImpl.class);
	
    private String redisKeyPrefix;

    private InventoryLoader loader;

    private Lock lock;

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
            markInaccurate(entityId);
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
                String lockKey = inventoryLockKey(entityId);
                try {
                    if(!lock.lock(lockKey, lockOwner)){return false;}
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
                    lock.unLock(lockKey, lockOwner);
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


    @Override
    public boolean store(final String entityId){
        final String inventoryKey = inventoryKey(entityId);
        ShardedJedis client = null;
        String lockOwner = "store";
        String lockKey = inventoryLockKey(entityId);
        try {
            if(!lock.lock(lockKey, lockOwner)){return false;}

            long loadedInventory = loader.load(entityId);
            client = pool.getResource();
            client.set(inventoryKey, String.valueOf(loadedInventory));
            return true;
        } catch (Exception e) {
            LOGGER.error("store failed:" + entityId, e);
            return false;
        } finally {
            lock.unLock(lockKey, lockOwner);
            if(client != null){
                client.close();
            }
        }
    }

    @Override
    public boolean storeIfNX(String entityId) {
        final String inventoryKey = inventoryKey(entityId);
        ShardedJedis client = null;
        String lockOwner = "storeIfNX";
        String lockKey = inventoryLockKey(entityId);
        try {
            if(!lock.tryLock(lockKey, lockOwner)){return false;}

            long loadedInventory = loader.load(entityId);
            client = pool.getResource();
            Long status = client.setnx(inventoryKey, String.valueOf(loadedInventory));
            return Long.valueOf(1).equals(status);
        } catch (Exception e) {
            LOGGER.error("store failed:" + entityId, e);
            return false;
        } finally {
            lock.unLock(lockKey, lockOwner);
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

    private boolean isInaccurate(String entityId){
        String key = redisKeyPrefix + ":inaccurate:" + entityId;
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            String value = client.get(key);
            return String.valueOf(1).equals(value);
        } catch (Exception e) {
            LOGGER.error("isInaccurate failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    @Override
    public boolean markInaccurate(String entityId){
        String key = redisKeyPrefix + ":inaccurate:" + entityId;
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            Long status = client.setnx(key, "1");
            return Long.valueOf(1).equals(status);
        } catch (Exception e) {
            LOGGER.error("markInaccurate failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    private boolean clearInaccurate(String entityId){
        String key = redisKeyPrefix + ":inaccurate:" + entityId;
        ShardedJedis client = null;
        try {
            client = pool.getResource();
            Long num = client.del(key);
            return num > 0;
        } catch (Exception e) {
            LOGGER.error("markInaccurate failed:" + entityId, e);
            return false;
        } finally {
            if(client != null){
                client.close();
            }
        }
    }

    @Override
    public boolean statusChanged(String entityId){
        try {
            long inventory = get(entityId);
            if(inventory > 0){
                return true;
            }

            boolean isInaccurate = isInaccurate(entityId);
            if(!isInaccurate){
                return true;
            }

            boolean isFinalStatus = loader.isFinalStatus(entityId);
            if(!isFinalStatus){
                return true;
            }

            boolean success = store(entityId);
            if(!success){
                return false;
            }

            return clearInaccurate(entityId);
        } catch (Exception e) {
            LOGGER.error("orderStatusChanged failed:" + entityId, e);
            return false;
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
        this.lock = new ShardedRedisLock(pool);
    }

}
