package org.sllx.site.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class StaticResourceHolder implements ApplicationContextAware,ServletConfigAware,ServletContextAware {

    public final static String USER_SESSION_NAME = "user";

    public final static String URL_ENCODING = "UTF-8";

    public final static String MAX_SIZE_PARAM = "maxUploadSize";

    public final static String APP_LOG_NAME = "org.sllx.site";

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
