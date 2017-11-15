package com.rainyalley.architecture.boot.config;

import com.rainyalley.architecture.boot.ErrorInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo defaultErrorHandler(Exception e) throws Exception {
        ErrorInfo info = new ErrorInfo();
        info.setMessage(e.getMessage());
        return info;
    }
}
