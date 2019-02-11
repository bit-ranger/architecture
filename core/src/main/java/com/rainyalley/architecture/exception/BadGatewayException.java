package com.rainyalley.architecture.exception;

/**
 * @author bin.zhang
 */
public class BadGatewayException extends SystemException {


    private static final long serialVersionUID = -3757443193865412020L;

    public BadGatewayException(Resource resource, String message, Throwable cause) {
        super(TaskStatus.BAD_GATEWAY, resource, message, cause);
    }

    public BadGatewayException(Resource resource, String message) {
        super(TaskStatus.BAD_GATEWAY, resource, message);
    }

    public BadGatewayException(Resource resource) {
        super(TaskStatus.BAD_GATEWAY, resource);
    }
}
