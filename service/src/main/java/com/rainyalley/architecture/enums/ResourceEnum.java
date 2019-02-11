package com.rainyalley.architecture.enums;

import com.rainyalley.architecture.exception.Resource;

/**
 * @author bin.zhang
 */

public enum  ResourceEnum implements Resource {

    /**
     * 用户
     */
    USER(100, "user");


    private int code;

    private String name;


    ResourceEnum(int code, String name) {
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
