package com.rainyalley.architecture.filter.limit;

import java.util.List;

public class CallerLimit {

    /**
     * 调用者
     */
    private String caller;

    /**
     * 最大并发量
     */
    private int maxConcurrency;

    /**
     * 最小调用间隔
     */
    private long minInterval;

    /**
     * 授权列表
     */
    private List<String> authorizedList;


    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
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

    public List<String> getAuthorizedList() {
        return authorizedList;
    }

    public void setAuthorizedList(List<String> authorizedList) {
        this.authorizedList = authorizedList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.CallerLimit\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"caller\":\"")
                .append(caller).append('\"');
        sb.append(",\"maxConcurrency\":")
                .append(maxConcurrency);
        sb.append(",\"minInterval\":")
                .append(minInterval);
        sb.append(",\"authorizedList\":")
                .append(authorizedList);
        sb.append('}');
        return sb.toString();
    }
}
