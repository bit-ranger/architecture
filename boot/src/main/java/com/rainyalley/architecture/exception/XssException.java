package com.rainyalley.architecture.exception;

/**
 * 跨站脚本攻击异常
 * @author bin.zhang
 */
public class XssException extends RuntimeException {

    private static final long serialVersionUID = -3684893066142405926L;

    public XssException(String message) {
        super(message);
    }
}
