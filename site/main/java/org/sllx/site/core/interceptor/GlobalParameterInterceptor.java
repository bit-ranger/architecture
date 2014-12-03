package org.sllx.site.core.interceptor;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalParameterInterceptor extends HandlerInterceptorAdapter{
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String root = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String resources = root + "/resources";
        String styles = resources + "/styles";
        String scripts = resources + "/scripts";
        request.setAttribute("root",root);
        request.setAttribute("resources",resources);
        request.setAttribute("styles",styles);
        request.setAttribute("scripts",scripts);
    }
}
