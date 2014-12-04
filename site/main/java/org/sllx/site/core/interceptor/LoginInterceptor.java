package org.sllx.site.core.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") == null){
            String redirectURL = request.getRequestURL().toString();
            redirectURL = URLEncoder.encode(redirectURL,"UTF-8");
            response.sendRedirect("/login/" + redirectURL);
            return false;
        }
        return true;
    }
}
