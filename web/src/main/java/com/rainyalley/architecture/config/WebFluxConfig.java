package com.rainyalley.architecture.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

@RestControllerAdvice
@Configuration
public class WebFluxConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.httpBasic();
        httpSecurity.formLogin();
        httpSecurity.authorizeExchange().anyExchange().permitAll();
        return httpSecurity.build();
    }

    @Bean
    WebSessionIdResolver cookieWebSessionIdResolver(){
        CookieWebSessionIdResolver cookieWebSessionIdResolver = new CookieWebSessionIdResolver();
        cookieWebSessionIdResolver.setCookieName("ARCHITECTURE_SESSION_ID");
        return cookieWebSessionIdResolver;
    }


    @Bean
    OptionalResultHandler optionalResultHandler(ErrorWebExceptionHandler webExceptionHandler,
                                                ResponseBodyResultHandler responseBodyResultHandler) {
        return new OptionalResultHandler(webExceptionHandler, responseBodyResultHandler);
    }


    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    TranslationExceptionHandler customExceptionHandler(){
        return new TranslationExceptionHandler();
    }
}
