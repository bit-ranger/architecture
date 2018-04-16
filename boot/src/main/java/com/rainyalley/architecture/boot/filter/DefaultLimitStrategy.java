package com.rainyalley.architecture.boot.filter;

import com.google.common.util.concurrent.RateLimiter;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的限制策略
 */
public class DefaultLimitStrategy implements LimitStrategy {

    @Override
    public RateLimiter getGlobalRateLimiter() {
        return null;
    }

    @Override
    public RateLimiter getGlobalRateLimiter(String callerId) {
        return null;
    }

    @Override
    public RateLimiter getTargetRateLimiter(String target) {
        return null;
    }

    @Override
    public RateLimiter getTargetRateLimiter(String target, String callerId) {
        return null;
    }

    @Override
    public boolean isValidCall(HttpServletRequest request) {
        return false;
    }

    @Override
    public boolean hasAuth(String target, String callerId) {
        return false;
    }

    @Override
    public int getMaxConcurrency() {
        return 0;
    }

    @Override
    public int getMaxConcurrency(String target) {
        return 0;
    }
}
