package com.rainyalley.architecture.boot.error;

public class ErrorInfo {

    public static final Integer ERROR = 400;
    private Integer code = ERROR;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
