package com.rainyalley.architecture.filter.limit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConsoleRedisImpl implements Console {

    /**
     * hash caller静态配置
     * keys: target, auth
     * value: maxConcurrency|minInterval, auth1|auth2|...
     */
    private static final String callerLimitKeyFmt = "limit:static:caller:${caller}";

    /**
     * hash caller运行时
     * keys: target|并发资源id
     * value: 最近使用时间
     */
    private static final String callerRtKeyFmt = "limit:rt:core:caller:${caller}";

    /**
     * list caller访问记录
     * 格式: target|time
     */
    private static final String callerRtAccessKeyFmt = "limit:rt:acc:caller:${caller}";

    /**
     * list caller并发资源池
     * 格式: int
     */
    private static final String callerConcPoolKeyFmt = "limit:rt:concupool:caller:${caller}:${target}";

    /**
     * list caller并发租借池
     * 格式: int
     */
    private static final String callerConcLeaseKeyFmt = "limit:rt:conculease:caller:${caller}:${target}";

    /**
     * list caller并发监控列表
     * 格式: caller|target
     */
    private static final String callerConcWatchingKey = "limit:watching:concu:caller";

    /**
     * hash target静态配置
     * keys: target
     * value: maxConcurrency|minInterval
     */
    private static final String targetLimitKey = "limit:static:target";

    /**
     * hash target运行时
     * keys: target|accessTimes, target|lastAccessTime, target|并发资源id
     * value: 访问次数, 最近访问时间, 最近使用时间
     */
    private static final String targetRtKey = "limit:rt:core:target";

    /**
     * list target并发资源池
     * 格式: int
     */
    private static final String targetConcPoolKeyFmt = "limit:rt:concupool:target:${target}";

    /**
     * list target并发租借池
     * 格式: int
     */
    private static final String targetConcLeaseKeyFmt = "limit:rt:conculease:target:${target}";

    /**
     * list target并发监控列表
     * 格式: target
     */
    private static final String targetConcWatchingKey = "limit:watching:concu:target";



    private JedisCommands jedis;

    public ConsoleRedisImpl(JedisCommands jedisCommands) {
        this.jedis = jedisCommands;
    }


    @Override
    public List<String> getCallerAuth(String caller) {
        String auth =  jedis.hget(callerKey(callerLimitKeyFmt, caller), "auth");

        if (auth == null){
            auth = jedis.hget(callerKey(callerLimitKeyFmt, "default"), "auth");
        }

        return List.of(auth.split("|"));
    }

    @Override
    public long getCallerAccessCount(String caller) {
        String crak = callerKey(callerRtAccessKeyFmt, caller);
        Long count =  jedis.llen(crak);
        return Objects.requireNonNullElse(count, 0L);
    }

    @Override
    public List<Access> getCallerAccessList(String caller, long start, long end) {
        String crak = callerKey(callerRtAccessKeyFmt, caller);
        List<String> vals = jedis.lrange(crak, start, end);
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

        String ctVal = jedis.hget(callerKey(callerLimitKeyFmt, caller), target);
        if(StringUtils.isBlank(ctVal)){
            ctVal = jedis.hget(callerKey(callerLimitKeyFmt, "default"), target);
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

        Long len = jedis.llen(callerTargetKey(callerConcLeaseKeyFmt, caller, target));
        if(len != null){
            cr.setCurrConcurrency(len.intValue());
        }
        return cr;
    }

    @Override
    public TargetLimit getTargetLimit(String target) {
        TargetLimit tl = new TargetLimit();
        tl.setTarget(target);
        String tVal = jedis.hget(targetLimitKey, target);
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

        List<String> vals = jedis.hmget(targetRtKey,concat(target, "accessTimes"), concat(target, "lastAccessTime"));
        if(CollectionUtils.isNotEmpty(vals)){
            if(StringUtils.isNotBlank(vals.get(0))){
                tr.setAccessTimes(Integer.valueOf(vals.get(0)));
            }
            if(StringUtils.isNotBlank(vals.get(1))){
                tr.setLastAccessTime(Long.valueOf(vals.get(1)));
            }
        }

        Long len = jedis.llen(targetKey(targetConcLeaseKeyFmt, target));
        if(len != null){
            tr.setCurrConcurrency(len.intValue());
        }
        return tr;
    }

    @Override
    public boolean acquireConcurrency(String caller, String target) {
        return false;
    }

    @Override
    public boolean releaseConcurrency(String caller, String target) {
        return false;
    }

    @Override
    public boolean hasAuth(String caller, String target) {
        return this.getCallerAuth(caller).contains(target);
    }

    @Override
    public boolean access(String caller, String target) {
        String targetRtKey = ConsoleRedisImpl.targetRtKey;
        jedis.hincrBy(targetRtKey, concat(target, "accessTimes"), 1);
        jedis.hset(targetRtKey, concat(target, "lastAccessTime"), String.valueOf(System.currentTimeMillis()));

        String callerRtAccessKey = callerKey(callerRtAccessKeyFmt, caller);
        jedis.lpush(callerRtAccessKey, concat(target, String.valueOf(System.currentTimeMillis())));
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
