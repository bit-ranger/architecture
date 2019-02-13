package com.rainyalley.architecture.exception;

/**
 * 客户端异常，由于用户输入的内容产生的异常
 * 用户变更输入内容后，将可以避免此异常
 * @author bin.zhang
 */
public class ClientException extends BaseException {

    private static final long serialVersionUID = 5616008549643796973L;


    public ClientException(TaskStatus bizStatus, Resource resource, String message, Throwable cause) {
        super(bizStatus, resource, message, cause);
        assertParam(bizStatus, resource);
    }

    public ClientException(TaskStatus bizStatus, Resource resource, String message) {
        super(bizStatus, resource, message);
        assertParam(bizStatus, resource);
    }

    public ClientException(TaskStatus bizStatus, Resource resource) {
        super(bizStatus, resource);
        assertParam(bizStatus, resource);
    }

    private void assertParam(TaskStatus bizStatus, Resource resource){
        if(!bizStatus.is4xxClientError()){
            throw new UnsupportedOperationException();
        }
    }
}
