package com.rainyalley.architecture.exception;


/**
 * <p>由不正确的参数格式产生的异常, 区别于{@link UnProcessableException}
 * <p>例如，参数范围不满足
 * 该异常只用于驳回不合法输入，无需提供异常栈
 * @author bin.zhang
 */
public class BadArgException extends ClientException{

    private static final long serialVersionUID = 695710555317104325L;

    public BadArgException(Resource resource){
        super(TaskStatus.BAD_REQUEST, resource);
    }

}
