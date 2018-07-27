package com.rainyalley.architecture.filter.limit.redis;

class Constant {
    /**
     * hash caller静态配置
     * keys: target, auth
     * value: maxConcurrency|minInterval, auth1|auth2|...
     */
    static final String CALLER_LIMIT_KEY_FMT = "limit:static:caller:${caller}";
    /**
     * hash caller运行时
     * keys: target|并发资源id
     * value: 最近使用时间
     */
    static final String CALLER_RT_KEY_FMT = "limit:rt:core:caller:${caller}";
    /**
     * list caller访问记录
     * 格式: target|time
     */
    static final String CALLER_RT_ACCESS_KEY_FMT = "limit:rt:acc:caller:${caller}";
    /**
     * list caller并发资源池
     * 格式: int
     */
    static final String CALLER_TARGET_CONC_POOL_KEY_FMT = "limit:rt:concupool:caller:${caller}:${target}";
    /**
     * list caller并发租借池
     * 格式: int
     */
    static final String CALLER_TARGET_CONC_LEASE_KEY_FMT = "limit:rt:conculease:caller:${caller}:${target}";
    /**
     * zset caller并发监控集
     * 格式: caller|target
     */
    static final String CALLER_CONC_WATCHING_KEY = "limit:watching:concu:caller";
    /**
     * hash target静态配置
     * keys: target
     * value: maxConcurrency|minInterval|maxExpend
     */
    static final String TARGET_LIMIT_KEY = "limit:static:target";
    /**
     * hash target运行时
     * keys: target|accessTimes, target|lastAccessTime, target|并发资源id
     * value: 访问次数, 最近访问时间, 最近使用时间
     */
    static final String TARGET_RT_KEY = "limit:rt:core:target";
    /**
     * list target并发资源池
     * 格式: int
     */
    static final String TARGET_CONC_POOL_KEY_FMT = "limit:rt:concupool:target:${target}";
    /**
     * list target并发租借池
     * 格式: int
     */
    static final String TARGET_CONC_LEASE_KEY_FMT = "limit:rt:conculease:target:${target}";
    /**
     * zset target并发监控集
     * 格式: target
     */
    static final String TARGET_CONC_WATCHING_KEY = "limit:watching:concu:target";
    /**
     * zset 花名册, 维护当前存活的机器列表
     */
    static final String ROSTER_KEY = "limit:watching:roster";
    /**
     * 资源协调锁的key
     */
    static final String COORDINATE_KEY = "limit:watching:coordinate";
}
