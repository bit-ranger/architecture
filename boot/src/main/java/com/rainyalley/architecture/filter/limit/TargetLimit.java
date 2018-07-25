package com.rainyalley.architecture.filter.limit;

public class TargetLimit {

    private String target;

    /**
     * 最大并发量
     */
    private int maxConcurrency;

    /**
     * 最小调用间隔
     */
    private long minInterval;

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
        sb.append('}');
        return sb.toString();
    }
}
