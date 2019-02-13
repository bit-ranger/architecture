package com.rainyalley.architecture.exception;


/**
 * 资源未找到的异常
 * @author bin.zhang
 */
public class NotFoundException extends ClientException{

    private static final long serialVersionUID = 695710555317104325L;

    public NotFoundException(Resource resource){
        super(TaskStatus.NOT_FOUND, resource);
    }

}
