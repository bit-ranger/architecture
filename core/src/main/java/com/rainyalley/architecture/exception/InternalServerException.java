package com.rainyalley.architecture.exception;

/**
 * @author bin.zhang
 */
public class InternalServerException extends SystemException {


    private static final long serialVersionUID = -3757443193865412020L;

    public InternalServerException(Resource resource, String message, Throwable cause) {
        super(TaskStatus.INTERNAL_SERVER_ERROR, resource, message, cause);
    }

    public InternalServerException(Resource resource, String message) {
        super(TaskStatus.INTERNAL_SERVER_ERROR, resource, message);
    }

    public InternalServerException(Resource resource) {
        super(TaskStatus.INTERNAL_SERVER_ERROR, resource);
    }
}
