package com.rainyalley.architecture.vo;

import lombok.Getter;

/**
 * @author bin.zhang
 */
@Getter
public class Result {

    private int statusCode;

    private String text;

    public Result(org.springframework.http.HttpStatus status) {
        this.statusCode = status.value();
        this.text = status.getReasonPhrase();
    }

    public Result(int statusCode, String text) {
        this.statusCode = statusCode;
        this.text = text;
    }
}