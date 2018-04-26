package com.rainyalley.architecture.filter;

import java.util.Set;

public class GlobalLimitInfo {

    private int maxConcurrency;

    private Set<String> validTargetSet;

    private long permitsPerSecond;

    private long warmupPeriod;

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public void setMaxConcurrency(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    public Set<String> getValidTargetSet() {
        return validTargetSet;
    }

    public void setValidTargetSet(Set<String> validTargetSet) {
        this.validTargetSet = validTargetSet;
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
