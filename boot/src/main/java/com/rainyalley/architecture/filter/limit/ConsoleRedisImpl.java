package com.rainyalley.architecture.filter.limit;

import redis.clients.jedis.JedisCommands;

public class ConsoleRedisImpl implements Console {


    /**
     * hash caller静态配置
     * keys: global, target, authorizedList
     * value: maxConcurrency|minInterval, maxConcurrency|minInterval, auth1|auth2|...
     */
    private static final String callerLmtKeyFmt = "limit:static:caller:${caller}";

    /**
     * hash caller运行时
     * keys: target|并发资源id
     * value: 最近使用时间
     */
    private static final String callerRtKeyFmt = "limit:rt:core:caller:${caller}";

    /**
     * hash caller访问记录
     * 格式: target|time
     */
    private static final String callerRtAccKeyFmt = "limit:rt:acc:caller:${caller}";

    /**
     * list caller并发资源池
     * 格式: int
     */
    private static final String callerConcurrencyPoolKey = "limit:rt:concupool:caller:${caller}:${target}";

    /**
     * list caller并发租借池
     * 格式: int
     */
    private static final String callerConcurrencyLeaseKey = "limit:rt:conculease:caller:${caller}:${target}";

    /**
     * list caller并发监控列表
     * 格式: caller|target
     */
    private static final String callerConcurrencyWatchingKey = "limit:watching:concu:caller";

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
    private static final String targetRuntimeKey = "limit:rt:core:target";

    /**
     * list target并发资源池
     * 格式: int
     */
    private static final String targetConcurrencyPoolKey = "limit:rt:concupool:target:${target}";

    /**
     * list target并发租借池
     * 格式: int
     */
    private static final String targetConcurrencyLeaseKey = "limit:rt:conculease:target:${target}";

    /**
     * list target并发监控列表
     * 格式: target
     */
    private static final String targetConcurrencyWatchingKey = "limit:watching:concu:target";






    private JedisCommands jedis;

    public ConsoleRedisImpl(JedisCommands jedisCommands) {
        this.jedis = jedisCommands;
    }

    @Override
    public CallerLimit getCallerLimit(String caller) {
        return null;
    }

    @Override
    public CallerRuntime getCallerRuntime(String caller) {
        return null;
    }

    @Override
    public TargetLimit getTargetLimit(String target) {
        return null;
    }

    @Override
    public TargetRuntime getTargetRuntime(String target) {
        return null;
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
        String defaultCallerLimitKey = callerKey(callerLmtKeyFmt, "default");
        String callerLimitKey  = callerKey(callerLimitKey, caller);
        String authorizedList =  jedis.hget(defaultCallerLimit, "authorizedList");
        return authorizedList.contains(target);
    }

    @Override
    public boolean access(String caller, String target) {
        String targetRtKey = targetRuntimeKey;
        jedis.hincrBy(targetRtKey, concat(target, "accessTimes"), 1);
        jedis.hset(targetRtKey, concat(target, "lastAccessTime"), String.valueOf(System.currentTimeMillis()));

        String callerRtAccessKey = callerKey(callerRtAccKeyFmt, caller);
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
