package com.rainyalley.architecture.boot.filter;

import com.google.common.util.concurrent.RateLimiter;

import javax.servlet.http.HttpServletRequest;

public interface LimitStrategy {

    /**
     *
     * @return 全局吞吐量控制
     */
    RateLimiter getGlobalRateLimiter();

    /**
     *
     * @return 指定用户全局吞吐量控制
     */
    RateLimiter getGlobalRateLimiter(String callerId);

    /**
     *
     * @return 指定接口吞吐量控制
     */
    RateLimiter getTargetRateLimiter(String target);

    /**
     *
     * @return 指定用户与接口吞吐量控制
     */
    RateLimiter getTargetRateLimiter(String target, String callerId);


    /**
     * @param request
     * @return 是否有效的请求
     */
    boolean isValidCall(HttpServletRequest request);

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
