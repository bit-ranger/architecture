package org.sllx.site.core.interceptor;


import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalParameterInterceptor extends HandlerInterceptorAdapter{
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView == null){
            return;
        }
        String root = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String resources = root + "/resources";
        String styles = resources + "/styles";
        String scripts = resources + "/scripts";
        ModelMap modelMap = modelAndView.getModelMap();
        modelMap.addAttribute("root", root);
        modelMap.addAttribute("resources", resources);
        modelMap.addAttribute("scripts", scripts);
        modelMap.addAttribute("styles", styles);
    }
}
