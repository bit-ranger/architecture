package com.rainyalley.architecture.filter.limit;

public class AccessInfo {

    private String target;

    private long accessTime;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.filter.limit.AccessInfo\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"target\":\"")
                .append(target).append('\"');
        sb.append(",\"accessTime\":")
                .append(accessTime);
        sb.append('}');
        return sb.toString();
    }
}
