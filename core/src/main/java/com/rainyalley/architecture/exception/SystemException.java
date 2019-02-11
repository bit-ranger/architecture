package com.rainyalley.architecture.exception;


/**
 * 系统异常, 无法预知的系统运行时异常
 * 变更输入参数后，依然无法避免此异常
 * 需要提供异常栈
 * @author bin.zhang
 */
public class SystemException extends BaseException {

    private static final long serialVersionUID = 3671068788381354045L;

    public SystemException(TaskStatus bizStatus, Resource resource, String message, Throwable cause) {
        super(bizStatus, resource, message, cause);
    }

    public SystemException(TaskStatus bizStatus, Resource resource, String message) {
        super(bizStatus, resource, message);
    }

    public SystemException(TaskStatus bizStatus, Resource resource) {
        super(bizStatus, resource);
    }
}
