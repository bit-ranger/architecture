package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.exception.BaseException;
import com.rainyalley.architecture.exception.ClientException;
import com.rainyalley.architecture.exception.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;

/**
 * @author bin.zhang
 */
@Slf4j
public class CustomExceptionResolver implements HandlerExceptionResolver {

    private String errorPath;

    public CustomExceptionResolver(String errorPath) {
        this.errorPath = errorPath;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof BaseException){
            return baseExceptionHandler((BaseException) ex, request);
        } else if(ex instanceof ConstraintViolationException){
            return constraintViolationExceptionHandler((ConstraintViolationException) ex, request);
        } else {
            return globalExceptionHandler(ex, request);
        }
    }


    private ModelAndView baseExceptionHandler(BaseException ex,
                                             HttpServletRequest request) {
        if(!(ex instanceof ClientException)){
            log.error(ex.getMessage(), ex);
        }
        HttpStatus httpStatus = null;
        try {
            httpStatus = HttpStatus.valueOf(ex.getTaskStatus().value());
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, httpStatus.value());
        request.setAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE, ex.getMessage());
        return new ModelAndView("forward:" + errorPath);
    }

    private ModelAndView constraintViolationExceptionHandler(ConstraintViolationException ex, HttpServletRequest request){
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST.value());
        ConstraintViolation violation= ex.getConstraintViolations().stream().findFirst().get();
        Path.Node last = getLastElement(violation.getPropertyPath());
        String message = last.getName() + " " + violation.getMessage();
        request.setAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE, message);
        return new ModelAndView("forward:" + errorPath);
    }


    private static <T> T getLastElement(final Iterable<T> elements) {
        final Iterator<T> itr = elements.iterator();
        T lastElement = itr.next();
        while(itr.hasNext()) {
            lastElement=itr.next();
        }
        return lastElement;
    }

    private ModelAndView globalExceptionHandler(Exception ex,
                                                HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, TaskStatus.INTERNAL_SERVER_ERROR.value());
        request.setAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE, ex.getMessage());
        return new ModelAndView("forward:" + errorPath);
    }
}
