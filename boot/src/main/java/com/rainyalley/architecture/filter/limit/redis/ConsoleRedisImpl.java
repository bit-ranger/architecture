package com.rainyalley.architecture.filter.limit.redis;

import com.rainyalley.architecture.filter.limit.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConsoleRedisImpl implements Console {

    private JedisCluster jedisCluster;

    public ConsoleRedisImpl(JedisCluster jedisCommands) {
        this.jedisCluster = jedisCommands;
    }


    @Override
    public List<String> getCallerAuth(String caller) {
        String auth =  jedisCluster.hget(callerKey(Constant.CALLER_LIMIT_KEY_FMT, caller), "auth");

        if (auth == null){
            auth = jedisCluster.hget(callerKey(Constant.CALLER_LIMIT_KEY_FMT, "default"), "auth");
        }

        return List.of(auth.split("|"));
    }

    @Override
    public long getCallerAccessCount(String caller) {
        String crak = callerKey(Constant.CALLER_RT_ACCESS_KEY_FMT, caller);
        Long count =  jedisCluster.llen(crak);
        return Objects.requireNonNullElse(count, 0L);
    }

    @Override
    public List<Access> getCallerAccessList(String caller, long start, long end) {
        String crak = callerKey(Constant.CALLER_RT_ACCESS_KEY_FMT, caller);
        List<String> vals = jedisCluster.lrange(crak, start, end);
        List<Access> accessList = Collections.emptyList();
        if(CollectionUtils.isNotEmpty(vals)){
            accessList = new ArrayList<>(vals.size());
            for (String val : vals) {
                if(StringUtils.isNotBlank(val)){
                    String[] accVal = val.split("|");
                    Access access = new Access();
                    access.setCaller(caller);
                    access.setTarget(accVal[0]);
                    access.setTime(Long.valueOf(accVal[1]));
                    accessList.add(access);
                }
            }
        }
        return accessList;
    }

    @Override
    public CallerLimit getCallerLimit(String caller, String target) {
        CallerLimit cl = new CallerLimit();
        cl.setCaller(caller);
        cl.setTarget(target);

        String ctVal = jedisCluster.hget(callerKey(Constant.CALLER_LIMIT_KEY_FMT, caller), target);
        if(StringUtils.isBlank(ctVal)){
            ctVal = jedisCluster.hget(callerKey(Constant.CALLER_LIMIT_KEY_FMT, "default"), target);
        }

        if(StringUtils.isNoneBlank(ctVal)){
            String[] vals = ctVal.split("|");
            cl.setMaxConcurrency(Integer.valueOf(vals[0]));
            cl.setMinInterval(Integer.valueOf(vals[1]));
            return cl;
        }

        return cl;
    }

    @Override
    public CallerRuntime getCallerRuntime(String caller, String target) {
        CallerRuntime cr = new CallerRuntime();
        cr.setCaller(caller);
        cr.setTarget(target);

        Long len = jedisCluster.llen(callerTargetKey(Constant.CALLER_CONC_LEASE_KEY_FMT, caller, target));
        if(len != null){
            cr.setCurrConcurrency(len.intValue());
        }
        return cr;
    }

    @Override
    public TargetLimit getTargetLimit(String target) {
        TargetLimit tl = new TargetLimit();
        tl.setTarget(target);
        String tVal = jedisCluster.hget(Constant.TARGET_LIMIT_KEY, target);
        if(StringUtils.isNoneBlank(tVal)){
            String[] vals = tVal.split("|");
            tl.setMaxConcurrency(Integer.valueOf(vals[0]));
            tl.setMinInterval(Integer.valueOf(vals[1]));
        }
        return tl;
    }

    @Override
    public TargetRuntime getTargetRuntime(String target) {
        TargetRuntime tr = new TargetRuntime();
        tr.setTarget(target);

        List<String> vals = jedisCluster.hmget(Constant.TARGET_RT_KEY,concat(target, "accessTimes"), concat(target, "lastAccessTime"));
        if(CollectionUtils.isNotEmpty(vals)){
            if(StringUtils.isNotBlank(vals.get(0))){
                tr.setAccessTimes(Integer.valueOf(vals.get(0)));
            }
            if(StringUtils.isNotBlank(vals.get(1))){
                tr.setLastAccessTime(Long.valueOf(vals.get(1)));
            }
        }

        Long len = jedisCluster.llen(targetKey(Constant.TARGET_CONC_LEASE_KEY_FMT, target));
        if(len != null){
            tr.setCurrConcurrency(len.intValue());
        }
        return tr;
    }

    @Override
    public boolean acquireConcurrency(String caller, String target) {
        jedisCluster.zadd(Constant.TARGET_CONC_WATCHING_KEY, System.currentTimeMillis(), target);
        String tConcId = jedisCluster.rpoplpush(targetKey(Constant.TARGET_CONC_POOL_KEY_FMT, target), targetKey(Constant.TARGET_CONC_LEASE_KEY_FMT, target));
        if(StringUtils.isBlank(tConcId)){
            return false;
        }
        jedisCluster.hset(Constant.TARGET_RT_KEY, concat(target, tConcId), String.valueOf(System.currentTimeMillis()));

        jedisCluster.zadd(Constant.CALLER_CONC_WATCHING_KEY, System.currentTimeMillis(), concat(caller, target));
        String cConcId = jedisCluster.rpoplpush(callerTargetKey(Constant.CALLER_CONC_POOL_KEY_FMT, caller, target), callerTargetKey(Constant.CALLER_CONC_LEASE_KEY_FMT, caller, target));
        if(StringUtils.isBlank(cConcId)){
            //归还一个target并发资源
            jedisCluster.rpoplpush(targetKey(Constant.TARGET_CONC_LEASE_KEY_FMT, target), targetKey(Constant.TARGET_CONC_POOL_KEY_FMT, target));
            return false;
        }
        jedisCluster.hset(callerKey(Constant.CALLER_RT_KEY_FMT, caller), concat(target, tConcId), String.valueOf(System.currentTimeMillis()));

        return true;
    }

    @Override
    public boolean releaseConcurrency(String caller, String target) {
        //归还一个target并发资源
        jedisCluster.rpoplpush(targetKey(Constant.TARGET_CONC_LEASE_KEY_FMT, target), targetKey(Constant.TARGET_CONC_POOL_KEY_FMT, target));
        //归还一个caller并发资源
        jedisCluster.rpoplpush(callerTargetKey(Constant.CALLER_CONC_LEASE_KEY_FMT, caller, target), callerTargetKey(Constant.CALLER_CONC_POOL_KEY_FMT, caller, target));
        return true;
    }

    @Override
    public boolean hasAuth(String caller, String target) {
        return this.getCallerAuth(caller).contains(target);
    }

    @Override
    public boolean access(String caller, String target) {
        String targetRtKey = Constant.TARGET_RT_KEY;
        jedisCluster.hincrBy(targetRtKey, concat(target, "accessTimes"), 1);
        jedisCluster.hset(targetRtKey, concat(target, "lastAccessTime"), String.valueOf(System.currentTimeMillis()));

        String callerRtAccessKey = callerKey(Constant.CALLER_RT_ACCESS_KEY_FMT, caller);
        jedisCluster.lpush(callerRtAccessKey, concat(target, String.valueOf(System.currentTimeMillis())));
        return false;
    }


    private String callerKey(String format, String caller){
        return format.replace("${caller}", caller);
    }

    private String targetKey(String format, String target){
        return format.replace("${target}", target);
    }

    private String callerTargetKey(String format, String caller, String target){
        return  format.replace("${caller}", caller).replace("${target}", target);
    }

    private String concat(String str1, String str2){
        return str1 + "|" + str2;
    }




}
