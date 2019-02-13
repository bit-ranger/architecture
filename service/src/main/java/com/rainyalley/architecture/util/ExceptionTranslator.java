package com.rainyalley.architecture.util;

import com.rainyalley.architecture.exception.*;
import org.springframework.dao.DuplicateKeyException;

/**
 * @author bin.zhang
 */
public class ExceptionTranslator {

    private Resource resource;

    public ExceptionTranslator(Resource resource) {
        this.resource = resource;
    }

    public BaseException translate(Throwable throwable){
        if(throwable instanceof BaseException){
            return (BaseException) throwable;
        }

        if(throwable instanceof DuplicateKeyException){
            return new ClientException(TaskStatus.CONFLICT, resource, TaskStatus.CONFLICT.getReasonPhrase(), throwable);
        }

        return new InternalServerException(resource, throwable.getMessage(), throwable);
    }

}
