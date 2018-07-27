package com.rainyalley.architecture.filter.limit.redis;

import com.rainyalley.architecture.util.jedis.JedisReentrantLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Patrol implements Runnable {

    /**
     * target最大消耗时间
     */
    private static final long DEFAULT_TARGET_MAX_EXPEND = 30000;

    private JedisCluster jedisCluster;

    private JedisReentrantLock coordinateLock;

    public Patrol(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
        this.coordinateLock = new JedisReentrantLock(Constant.COORDINATE_KEY, jedisCluster);
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

        //独占
        boolean locked = coordinateLock.tryLock(10000);
        if(!locked){
            return;
        }

        try {
            coordinate();
            jedisCluster.zrem(Constant.ROSTER_KEY, shockList.toArray(new String[0]));
        } finally {
            coordinateLock.unLock();
        }
    }

    public void patrol(){
        SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(this, 10, 10, TimeUnit.SECONDS);
    }

    private void coordinate(){
        Set<String> targetList =  jedisCluster.zrangeByScore(Constant.TARGET_CONC_WATCHING_KEY, 0, System.currentTimeMillis());
        for (String target : targetList) {
            coordinateTarget(target);
        }

        Set<String> callerTargetList =  jedisCluster.zrangeByScore(Constant.CALLER_CONC_WATCHING_KEY, 0, System.currentTimeMillis());
        for (String callerTarget : callerTargetList) {
            coordinateCallerTarget(callerTarget);
        }
    }

    private void coordinateTarget(String target){
        List<String> tConcIdList = jedisCluster.lrange(Util.targetKey(Constant.TARGET_CONC_LEASE_KEY_FMT, target), 0, -1);
        if(tConcIdList.isEmpty()){
            return;
        }

        List<String> hashFieldList = new ArrayList<>();
        for (String tConcId : tConcIdList) {
            hashFieldList.add(Util.concat(target, tConcId));
        }
        List<String> tConcLastUseList = jedisCluster.hmget(Constant.TARGET_RT_KEY, hashFieldList.toArray(new String[0]));

        String deadline = deadLine(target);
        //超时的全部归还
        for (int i = 0; i < tConcLastUseList.size(); i++) {
            String tConcId = tConcIdList.get(i);
            String tConcLastUse = tConcLastUseList.get(i);
            if(deadline.compareTo(tConcLastUse) > 0){
                //归还一个tConcId
                jedisCluster.rpoplpush(
                        Util.targetKey(Constant.TARGET_CONC_LEASE_KEY_FMT, target),
                        Util.targetKey(Constant.TARGET_CONC_POOL_KEY_FMT, target)
                );
                //归还的不一定是超时的资源, 所以修改该资源的last use时间, 以免再次归还
                //但此处可能发生中断, 依旧可能造成再次归还
                jedisCluster.hset(Constant.TARGET_RT_KEY, Util.concat(target, tConcId), String.valueOf(System.currentTimeMillis()));
            }
        }

    }

    private void coordinateCallerTarget(String callerTarget){
        if(StringUtils.isBlank(callerTarget)){
            return;
        }
        String[] ctVals = Util.split(callerTarget);
        String caller = ctVals[0];
        String target = ctVals[1];

        List<String> ctConcIdList = jedisCluster.lrange(Util.callerTargetKey(Constant.CALLER_TARGET_CONC_LEASE_KEY_FMT, caller, target), 0, -1);
        if(ctConcIdList.isEmpty()){
            return;
        }

        List<String> hashFieldList = new ArrayList<>();
        for (String ctConcId : ctConcIdList) {
            hashFieldList.add(Util.concat(target, ctConcId));
        }
        //所有conc的最后使用时间
        List<String> ctConcLastUseList = jedisCluster.hmget(Constant.TARGET_RT_KEY, hashFieldList.toArray(new String[0]));

        String deadline = deadLine(target);
        //超时的全部归还
        for (int i = 0; i < ctConcLastUseList.size(); i++) {
            String ctConcId = ctConcIdList.get(i);
            String ctConcLastUse = ctConcLastUseList.get(i);
            if(deadline.compareTo(ctConcLastUse) > 0){
                //归还一个ctConcId
                jedisCluster.rpoplpush(
                        Util.callerTargetKey(Constant.CALLER_TARGET_CONC_LEASE_KEY_FMT, caller, target),
                        Util.callerTargetKey(Constant.CALLER_TARGET_CONC_POOL_KEY_FMT, caller, target)
                );
                //归还的不一定是超时的资源, 所以修改该资源的last use时间, 以免再次归还
                //但此处可能发生中断, 依旧可能造成再次归还
                jedisCluster.hset(Util.callerKey(Constant.CALLER_RT_KEY_FMT, caller), Util.concat(target, ctConcId), String.valueOf(System.currentTimeMillis()));
            }
        }
    }

    private String deadLine(String target){
        //未超时时间前闸
        long maxExpend = DEFAULT_TARGET_MAX_EXPEND;
        String tVal = jedisCluster.hget(Constant.TARGET_LIMIT_KEY, target);
        if(StringUtils.isNoneBlank(tVal)){
            String[] vals = Util.split(tVal);
            maxExpend = Long.valueOf(vals[2]);
        }
        return String.valueOf(System.currentTimeMillis() - maxExpend);
    }
}
