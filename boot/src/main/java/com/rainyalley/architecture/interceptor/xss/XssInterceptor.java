package com.rainyalley.architecture.interceptor.xss;

import com.rainyalley.architecture.exception.BadRequestException;
import com.rainyalley.architecture.filter.xss.SimpleXssChecker;
import com.rainyalley.architecture.filter.xss.XssChecker;
import org.springframework.http.HttpMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class XssInterceptor extends HandlerInterceptorAdapter {

    private XssChecker xssChecker = new SimpleXssChecker();


    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception {
        if(HttpMethod.POST.name().equalsIgnoreCase(httpRequest.getMethod()) || HttpMethod.PUT.name().equalsIgnoreCase(httpRequest.getMethod())){
            if(httpRequest instanceof MultipartHttpServletRequest){
                for (Map.Entry<String,String[]> entry : httpRequest.getParameterMap().entrySet()) {
                    for (String s : entry.getValue()) {
                        if(!xssChecker.isValid(s)){
                            throw new BadRequestException(String.format("The parameter %s contains invalid tag", entry.getKey()));
                        }
                    }
                }
            }

        }

        return true;
    }

    public void setXssChecker(XssChecker xssChecker) {
        this.xssChecker = xssChecker;
    }

}
