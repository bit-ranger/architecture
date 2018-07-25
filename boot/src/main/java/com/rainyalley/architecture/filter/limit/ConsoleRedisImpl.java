package com.rainyalley.architecture.filter.limit;

import redis.clients.jedis.JedisCommands;

public class ConsoleRedisImpl implements Console {

    /**
     * hash
     */
    private static final String callerLimitKeyPrefix = "limit:static:caller:";

    /**
     * hash
     */
    private static final String targetLimitKeyPrefix = "limit:static:target:";

    /**
     * hash
     */
    private static final String callerRuntimeKeyPrefix = "limit:rt:core:caller:";

    /**
     * hash
     */
    private static final String callerRuntimeAccessInfoKeyPrefix = "limit:rt:accinfo:caller:";

    /**
     * hash
     */
    private static final String targetRuntimeKeyPrefix = "limit:rt:core:target:";

    /**
     * list
     */
    private static final String callerConcurrencyPoolKeyPrefix = "limit:rt:concupool:caller:";

    /**
     * list
     */
    private static final String callerConcurrencyLeaseKeyPrefix = "limit:rt:conculease:caller:";

    /**
     * list
     */
    private static final String callerConcurrencyWatchingKey = "limit:watching:concu:caller";

    /**
     * list
     */
    private static final String targetConcurrencyPoolKeyPrefix = "limit:rt:concupool:target:";

    /**
     * list
     */
    private static final String targetConcurrencyLeaseKeyPrefix = "limit:rt:conculease:target:";

    /**
     * list
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
        String authorizedList =  jedis.hget(key(callerLimitKeyPrefix, caller), "authorizedList");
        return authorizedList.contains(target);
    }

    @Override
    public boolean access(String caller, String target) {
        String targetRtKey = key(targetRuntimeKeyPrefix, target);
        jedis.hincrBy(targetRtKey, "accessTimes", 1);
        jedis.hset(targetRtKey, "lastAccessTime", String.valueOf(System.currentTimeMillis()));

        String callerRtAccessKey = key(callerRuntimeAccessInfoKeyPrefix, caller);
        jedis.lpush(callerRtAccessKey, target + "@" + System.currentTimeMillis());
        return false;
    }


    private String key(String prefix, String suffix){
        return prefix + ":" + suffix;
    }





}
