package com.rainyalley.architecture.web.user.interceptor;

import com.rainyalley.architecture.web.user.StaticResourceHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute(StaticResourceHolder.USER_SESSION_NAME) == null){
            String redirectURL = request.getRequestURL().toString();
            redirectURL = URLEncoder.encode(redirectURL,StaticResourceHolder.URL_ENCODING);
            response.sendRedirect("/login?redirectURL=" + redirectURL);
            return false;
        }
        return true;
    }
}
