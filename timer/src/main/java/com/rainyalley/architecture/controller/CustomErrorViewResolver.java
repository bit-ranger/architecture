package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author bin.zhang
 * 自定义的异常View处理器
 */
public class CustomErrorViewResolver extends DefaultErrorViewResolver {


    private ErrorAttributes errorAttributes;

    public CustomErrorViewResolver(ApplicationContext applicationContext, ResourceProperties resourceProperties) {
        super(applicationContext, resourceProperties);
    }

    @Override
    public int getOrder() {
        return super.getOrder() - 1;
    }

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView modelAndView = super.resolveErrorView(request, status, model);
        Map<String,Object> newModel = null;
        if(Objects.isNull(modelAndView)){
            newModel = new HashMap<>(model);
        } else {
            newModel = new HashMap<>(modelAndView.getModel());
        }

        Throwable throwable = errorAttributes.getError(new ServletWebRequest(request));
        int code = 0;
        if(throwable instanceof BaseException){
            code = ((BaseException) throwable).getResource().getCode();
            newModel.putIfAbsent("message", throwable.getMessage());
        }
        newModel.putIfAbsent("resourceCode", StringUtils.leftPad(String.valueOf(code), 3, '0'));

        if(newModel.get("errors") != null && newModel.get("errors") instanceof List && ((List) newModel.get("errors")).get(0) instanceof MessageSourceResolvable){
            MessageSourceResolvable resolvable = ((List<? extends MessageSourceResolvable>)newModel.get("errors")).get(0);
            if(resolvable instanceof FieldError){
                newModel.put("message", ((FieldError)resolvable).getField() + resolvable.getDefaultMessage());
            } else {
                newModel.put("message", resolvable.getDefaultMessage());
            }
        }

        if(Objects.isNull(modelAndView)){
            return new ModelAndView("error/5xx", Collections.unmodifiableMap(newModel));
        } else {
            return new ModelAndView(modelAndView.getViewName(), Collections.unmodifiableMap(newModel));
        }
    }

    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
}
