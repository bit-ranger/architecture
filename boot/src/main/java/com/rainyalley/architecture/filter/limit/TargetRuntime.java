package com.rainyalley.architecture.filter.limit;

public class TargetRuntime {

    private long accessTimes;

    private long lastAccessTime;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.TargetRuntime\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"accessTimes\":")
                .append(accessTimes);
        sb.append(",\"lastAccessTime\":")
                .append(lastAccessTime);
        sb.append(",\"currConcurrency\":")
                .append(currConcurrency);
        sb.append('}');
        return sb.toString();
    }
}
