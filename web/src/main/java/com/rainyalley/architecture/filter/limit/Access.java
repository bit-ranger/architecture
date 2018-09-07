package com.rainyalley.architecture.filter.limit;

import org.apache.commons.lang3.StringUtils;

public class Access {

    private String caller = StringUtils.EMPTY;

    private String target = StringUtils.EMPTY;

    private long time = 0;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.Access\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"caller\":\"")
                .append(caller).append('\"');
        sb.append(",\"target\":\"")
                .append(target).append('\"');
        sb.append(",\"time\":")
                .append(time);
        sb.append('}');
        return sb.toString();
    }
}
