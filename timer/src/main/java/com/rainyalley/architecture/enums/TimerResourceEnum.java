package com.rainyalley.architecture.enums;

import com.rainyalley.architecture.exception.Resource;

public enum TimerResourceEnum implements Resource {

    /**
     * cron expression
     */
    CRON(100, "cron");

    private int code;

    private String name;


    TimerResourceEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
