package com.rainyalley.architecture.exception;

/**
 * 跨站脚本攻击异常
 * @author bin.zhang
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -3684893066142405926L;

    public BadRequestException(String message) {
        super(message);
    }
}
