package com.rainyalley.architecture.filter.limit;

import org.apache.commons.lang3.StringUtils;

public class TargetLimit {

    private String target = StringUtils.EMPTY;

    /**
     * 最大并发, 数量
     */
    private int maxConcurrency = 0;

    /**
     * 最小调用间隔时间, 毫秒
     */
    private long minInterval = 0;

    /**
     * 最大花费时间, 毫秒
     */
    private long maxExpend = 0;

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

    public long getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(long minInterval) {
        this.minInterval = minInterval;
    }

    public long getMaxExpend() {
        return maxExpend;
    }

    public void setMaxExpend(long maxExpend) {
        this.maxExpend = maxExpend;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.TargetLimit\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"target\":\"")
                .append(target).append('\"');
        sb.append(",\"maxConcurrency\":")
                .append(maxConcurrency);
        sb.append(",\"minInterval\":")
                .append(minInterval);
        sb.append(",\"maxExpend\":")
                .append(maxExpend);
        sb.append('}');
        return sb.toString();
    }
}
