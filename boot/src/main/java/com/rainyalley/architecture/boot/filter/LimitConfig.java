package com.rainyalley.architecture.boot.filter;

import com.google.common.util.concurrent.RateLimiter;

public interface LimitConfig {

    /**
     *
     * @return 全局吞吐量控制
     */
    RateLimiter getRateLimiter();

    /**
     *
     * @return 每用户吞吐量控制
     */
    RateLimiter getRateLimiter(String callerId);

    /**
     *
     * @return 每接口吞吐量控制
     */
    RateLimiter getTargetRateLimiter(String target);

    /**
     *
     * @return 每用户每接口吞吐量控制
     */
    RateLimiter getTargetRateLimiter(String target, String callerId);

    /**
     *
     * @return 有调用权限
     */
    boolean hasAuth(String target, String callerId);

    /**
     *
     * @return 全局最大并发量
     */
    int getMaxConcurrency();

    /**
     *
     * @return 指定接口最大并发量
     */
    int getMaxConcurrency(String target);
}
