package com.rainyalley.architecture.filter.limit.redis;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import redis.clients.jedis.JedisCluster;

import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Patrol implements Runnable {

    private JedisCluster jedisCluster;

    public Patrol(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    /**
     * 线程池
     */
    private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(
            1, new CustomizableThreadFactory("limit-redis-patrol-"));

    @Override
    public void run() {
        // 20秒未在花名册报备的机器列表
        Set<String> shockList =  jedisCluster.zrangeByScore(Constant.ROSTER_KEY, 0, System.currentTimeMillis() - 20000);
        if (CollectionUtils.isEmpty(shockList)){
            return;
        }
    }

    public void patrol(){
        SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(this, 10, 10, TimeUnit.SECONDS);
    }
}
