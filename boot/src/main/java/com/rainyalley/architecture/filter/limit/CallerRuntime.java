package com.rainyalley.architecture.filter.limit;

import org.apache.commons.lang3.StringUtils;

public class CallerRuntime {

    /**
     * 调用者
     */
    private String caller = StringUtils.EMPTY;

    /**
     *  目标
     */
    private String target  = StringUtils.EMPTY;

    /**
     * 当前并发量
     */
    private int currConcurrency = 0;

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
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
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.CallerRuntime\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"caller\":\"")
                .append(caller).append('\"');
        sb.append(",\"target\":\"")
                .append(target).append('\"');
        sb.append(",\"currConcurrency\":")
                .append(currConcurrency);
        sb.append('}');
        return sb.toString();
    }
}
