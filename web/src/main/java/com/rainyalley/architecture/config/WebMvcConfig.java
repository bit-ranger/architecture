package com.rainyalley.architecture.config;

import com.rainyalley.architecture.exception.BadRequestException;
import com.rainyalley.architecture.exception.PreconditionException;
import com.rainyalley.architecture.exception.ServiceException;
import com.rainyalley.architecture.filter.limit.SimpleLimitFilter;
import com.rainyalley.architecture.filter.xss.XssFilter;
import com.rainyalley.architecture.interceptor.xss.XssInterceptor;
import com.rainyalley.architecture.vo.ErrorVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import redis.clients.jedis.JedisCluster;

import java.util.concurrent.TimeUnit;

@ControllerAdvice
@Configuration
@ServletComponentScan
public class WebMvcConfig implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        System.setProperty(freemarker.log.Logger.SYSTEM_PROPERTY_NAME_LOGGER_LIBRARY, String.valueOf(freemarker.log.Logger.LIBRARY_NAME_SLF4J));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        registry.addResourceHandler("/user/**")
//                .addResourceLocations("/user/")
//                .setCachePeriod(3600*24);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor webContent = new WebContentInterceptor();
        webContent.setCacheControl(CacheControl.maxAge(10, TimeUnit.HOURS));

        XssInterceptor xssInterceptor = new XssInterceptor();
        registry.addInterceptor(xssInterceptor).addPathPatterns("/**/*");
        registry.addInterceptor(webContent).addPathPatterns("/api/v1/user/**");
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean(){
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<XssFilter>();
        XssFilter filter = new XssFilter();
        registration.setOrder(0);
        registration.setFilter(filter);
        registration.setName("xssFilter");
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<SimpleLimitFilter> simpleLimitFilterRegistrationBean(JedisCluster jedisCluster){
        FilterRegistrationBean<SimpleLimitFilter> registration = new FilterRegistrationBean<SimpleLimitFilter>();
        SimpleLimitFilter filter = new SimpleLimitFilter();
        filter.setJedisCluster(jedisCluster);
        registration.setOrder(0);
        registration.setFilter(filter);
        registration.setName("simpleLimitFilter");
        registration.addUrlPatterns("/limit/*");
        return registration;
    }


    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseBody
    public ResponseEntity<ErrorVo> badRequestExceptionHandler(BadRequestException e) {
        ErrorVo info = new ErrorVo();
        info.setMessage(e.getMessage());
        info.setCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(info);
    }

    @ExceptionHandler(value = PreconditionException.class)
    @ResponseBody
    public ResponseEntity<ErrorVo> preconditionExceptionHandler(PreconditionException e) {
        ErrorVo info = new ErrorVo();
        info.setMessage(e.getMessage());
        info.setCode(HttpStatus.PRECONDITION_FAILED.value());
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(info);
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorVo> internalServerErrorExceptionHandler(ServiceException e) {
        logger.error(e.getMessage(), e);
        ErrorVo info = new ErrorVo();
        info.setMessage(e.getMessage());
        info.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(info);
    }

}
