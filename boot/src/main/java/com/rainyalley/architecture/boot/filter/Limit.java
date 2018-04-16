package com.rainyalley.architecture.boot.filter;

import com.google.common.util.concurrent.RateLimiter;

public class Limit {

    private RateLimiter rateLimiter;

    private int maxConcurrency;

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public void setMaxConcurrency(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }
}
