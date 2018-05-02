package com.rainyalley.architecture.interceptor.xss;

import com.rainyalley.architecture.exception.XssException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * XSS拦截器
 * @author bin.zhang
 */
public class JsoupXssInterceptor extends HandlerInterceptorAdapter {

    private static final Whitelist WHITELIST = Whitelist.basic();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (Map.Entry<String,String[]> entry : request.getParameterMap().entrySet()) {
            for (String s : entry.getValue()) {
                if(!Jsoup.isValid(s, WHITELIST)){
                    throw new XssException(String.format("The parameter %s contains invalid XSS tag", entry.getKey()));
                }
            }
        }

        return true;
    }
}
