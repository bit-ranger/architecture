package com.rainyalley.architecture.filter.limit;

import org.apache.commons.lang3.StringUtils;

public class TargetRuntime {

    private String target = StringUtils.EMPTY;

    private long accessTimes = 0;

    private long lastAccessTime = 0;

    /**
     * 当前并发量
     */
    private int currConcurrency;

    public long getAccessTimes() {
        return accessTimes;
    }

    public void setAccessTimes(long accessTimes) {
        this.accessTimes = accessTimes;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public int getCurrConcurrency() {
        return currConcurrency;
    }

    public void setCurrConcurrency(int currConcurrency) {
        this.currConcurrency = currConcurrency;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.TargetRuntime\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"target\":\"")
                .append(target).append('\"');
        sb.append(",\"accessTimes\":")
                .append(accessTimes);
        sb.append(",\"lastAccessTime\":")
                .append(lastAccessTime);
        sb.append(",\"currConcurrency\":")
                .append(currConcurrency);
        sb.append('}');
        return sb.toString();
    }
}
