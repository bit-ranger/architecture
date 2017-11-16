package com.rainyalley.architecture.boot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@ServletComponentScan
public class WebMvcConfig {


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
