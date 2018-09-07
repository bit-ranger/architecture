package com.rainyalley.architecture.filter.limit.redis;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Keep implements Runnable{

    private static final String ID = UUID.randomUUID().toString();

    /**
     * 线程池
     */
    private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(
            1, new CustomizableThreadFactory("limit-redis-keep-"));

    private JedisCluster jedisCluster;

    public Keep(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public void run() {
        jedisCluster.zadd(Constant.ROSTER_KEY, System.currentTimeMillis(), ID);
    }

    public void keep(){
        SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(this, 10, 10, TimeUnit.SECONDS);
    }
}
