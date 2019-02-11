package com.rainyalley.architecture.config;

import com.rainyalley.architecture.exception.BaseException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

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
    OptionalResultHandler optionalResultHandler(ErrorWebExceptionHandler webExceptionHandler,
                                                ResponseBodyResultHandler responseBodyResultHandler) {
        return new OptionalResultHandler(webExceptionHandler, responseBodyResultHandler);
    }


    @ExceptionHandler(BaseException.class)
    public <R> Mono<R> baseExceptionHandler(BaseException baseEx) {
        return Mono.defer(() -> {
            Exception ex = new ResponseStatusException(HttpStatus.valueOf(baseEx.getTaskStatus().value()), baseEx.getMessage());
            return Mono.error(ex);
        });
    }
}
