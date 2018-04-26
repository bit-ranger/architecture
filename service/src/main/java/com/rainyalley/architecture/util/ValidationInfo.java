package com.rainyalley.architecture.util;

import java.util.Map;

public class ValidationInfo {
    private String id;
    private Map<String, Object> constraint;

    public ValidationInfo(String id, Map<String, Object> constraint) {
        this.id = id;
        this.constraint = constraint;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getConstraint() {
        return constraint;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValidationInfo{");
        sb.append("id='").append(id).append('\'');
        sb.append(", constraint=").append(constraint);
        sb.append('}');
        return sb.toString();
    }
}
