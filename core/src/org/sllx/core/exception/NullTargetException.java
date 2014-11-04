package org.sllx.core.exception;

/**
 * Created by sllx on 14-11-4.
 */
public class NullTargetException extends Exception{

    public NullTargetException() {
        super();
    }

    public NullTargetException(String message) {
        super(message);
    }

    public NullTargetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullTargetException(Throwable cause) {
        super(cause);
    }

    protected NullTargetException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
