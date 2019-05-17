package com.rainyalley.architecture.config;

import com.rainyalley.architecture.controller.CustomErrorViewResolver;
import com.rainyalley.architecture.controller.CustomExceptionResolver;
import com.rainyalley.architecture.impl.TimerServiceQuartzImpl;
import com.spring4all.swagger.EnableSwagger2Doc;
import com.spring4all.swagger.SwaggerProperties;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@EnableSwagger2Doc
@EnableScheduling
@Configuration
public class TimerConfig implements WebMvcConfigurer, InitializingBean {

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;


    @Autowired
    private BuildProperties buildProperties;


    @Autowired
    private SwaggerProperties swaggerProperties;


    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    CustomErrorViewResolver customErrorViewResolver(ApplicationContext applicationContext, ResourceProperties resourceProperties, ErrorAttributes errorAttributes){
        CustomErrorViewResolver resolver = new CustomErrorViewResolver(applicationContext, resourceProperties);
        resolver.setErrorAttributes(errorAttributes);
        return resolver;
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new CustomExceptionResolver(errorPath));
    }

    @Bean
    TimerServiceQuartzImpl timerService(Scheduler scheduler){
        TimerServiceQuartzImpl timerService = new TimerServiceQuartzImpl();
        timerService.setScheduler(scheduler);
        return timerService;
    }

    @ConfigurationProperties(prefix = "timer")
    @Bean
    TimerProperties timerProperties(){
        return new TimerProperties();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SwaggerProperties.GlobalResponseMessage globalResponseMessage = new SwaggerProperties.GlobalResponseMessage();
        SwaggerProperties.GlobalResponseMessageBody body = new SwaggerProperties.GlobalResponseMessageBody();
        body.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.setModelRef("ApiError");
        List<SwaggerProperties.GlobalResponseMessageBody> bodies = Arrays.asList(body);
        globalResponseMessage.setPost(bodies);
        globalResponseMessage.setGet(bodies);
        globalResponseMessage.setDelete(bodies);
        globalResponseMessage.setPut(bodies);
        globalResponseMessage.setPatch(bodies);
        globalResponseMessage.setHead(bodies);
        globalResponseMessage.setOptions(bodies);
        globalResponseMessage.setTrace(bodies);
        swaggerProperties.setGlobalResponseMessage(globalResponseMessage);
        swaggerProperties.setVersion(buildProperties.getVersion());
    }
}
