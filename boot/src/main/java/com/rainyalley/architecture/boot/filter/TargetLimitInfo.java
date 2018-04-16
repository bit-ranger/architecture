package com.rainyalley.architecture.boot.filter;

public class TargetLimitInfo {

    private String target;

    private int maxConcurrency;

    private long permitsPerSecond;

    private long warmupPeriod;


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public void setMaxConcurrency(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    public long getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(long permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    public long getWarmupPeriod() {
        return warmupPeriod;
    }

    public void setWarmupPeriod(long warmupPeriod) {
        this.warmupPeriod = warmupPeriod;
    }
}
