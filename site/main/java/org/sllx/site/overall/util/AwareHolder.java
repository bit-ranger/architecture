package org.sllx.site.overall.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Created by sllx on 14-11-4.
 */
public class AwareHolder implements ApplicationContextAware,ServletConfigAware,ServletContextAware {

    private static ApplicationContext applicationContext;

    private static ServletConfig servletConfig;

    private static ServletContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public static ServletConfig getServletConfig() {
        return servletConfig;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
