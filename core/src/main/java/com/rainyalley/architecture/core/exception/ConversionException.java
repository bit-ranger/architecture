package com.rainyalley.architecture.core.exception;

/**
 * 数据转换异常
 */
public class ConversionException extends RuntimeException {

    private Object origin;

    private Class<?> targetType;

    public ConversionException(Class<?> clz, Object origin) {
        super(String.format("For class %s from %s", clz.getName(), origin));
    }

    public ConversionException(Class<?> clz, Object origin, Throwable cause) {
        super(String.format("For class %s from %s", clz.getName(), origin), cause);
    }
}
