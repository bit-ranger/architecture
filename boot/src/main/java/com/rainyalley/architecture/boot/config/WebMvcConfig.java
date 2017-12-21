package com.rainyalley.architecture.boot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.concurrent.TimeUnit;

@Configuration
@ServletComponentScan
public class WebMvcConfig extends WebMvcConfigurerAdapter{


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

//        registry.addResourceHandler("/user/**")
//                .addResourceLocations("/user/")
//                .setCachePeriod(3600*24);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor webContent = new WebContentInterceptor();
        webContent.setCacheControl(CacheControl.maxAge(10, TimeUnit.HOURS));


        registry.addInterceptor(webContent).addPathPatterns("/user/**");
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean characterEncodingFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CharacterEncodingFilter filter =  new CharacterEncodingFilter();
        registration.setOrder(1);
        registration.setFilter(filter);
        registration.addInitParameter("encoding", "UTF-8");
        registration.addInitParameter("forceEncoding", "true");
        registration.setName("encodingFilter");
        registration.addUrlPatterns("*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean multipartFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        HiddenHttpMethodFilter filter =  new HiddenHttpMethodFilter();
        registration.setOrder(1);
        registration.setFilter(filter);
        registration.setName("hiddenHttpMethodFilter");
        registration.addServletNames("dispatcherServlet");
        return registration;
    }

}
