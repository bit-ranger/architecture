package com.rainyalley.architecture.filter.limit;

import java.util.List;

public class CallerRuntime {

    /**
     * 调用者
     */
    private String caller;

    /**
     * 当前并发量
     */
    private int currConcurrency;

    /**
     * 访问记录
     */
    private List<AccessInfo> accessInfoList;

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

    public List<AccessInfo> getAccessInfoList() {
        return accessInfoList;
    }

    public void setAccessInfoList(List<AccessInfo> accessInfoList) {
        this.accessInfoList = accessInfoList;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.CallerRuntime\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"caller\":\"")
                .append(caller).append('\"');
        sb.append(",\"currConcurrency\":")
                .append(currConcurrency);
        sb.append(",\"accessInfoList\":")
                .append(accessInfoList);
        sb.append('}');
        return sb.toString();
    }
}
