package com.rainyalley.architecture.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;

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
    OptionalResultHandler optionalResultHandler(ErrorWebExceptionHandler webExceptionHandler,
                                                ResponseBodyResultHandler responseBodyResultHandler) {
        return new OptionalResultHandler(webExceptionHandler, responseBodyResultHandler);
    }
}
