package com.rainyalley.architecture.exception;


/**
 * <p>由不合法输入产生的异常, 区别于{@link BadArgException} 该异常用于格式正确，但内容有误造成无法处理的场景
 * <p>例如，解密失败
 * 该异常只用于驳回不合法输入，无需提供异常栈
 * @author bin.zhang
 */
public class UnProcessableException extends ClientException {

    private static final long serialVersionUID = 1729118392143342039L;

    public UnProcessableException(Resource resourceEnum, String message) {
        super(TaskStatus.UNPROCESSABLE_ENTITY, resourceEnum, message);
    }

    public UnProcessableException(Resource resourceEnum){
        super(TaskStatus.UNPROCESSABLE_ENTITY, resourceEnum);
    }
}
