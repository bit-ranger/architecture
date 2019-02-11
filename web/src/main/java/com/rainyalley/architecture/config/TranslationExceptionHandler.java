package com.rainyalley.architecture.config;

import com.rainyalley.architecture.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

public class TranslationExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if(ex instanceof BaseException){
            return baseExceptionHandler((BaseException)ex);
        }

        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

    }


    private  <R> Mono<R> baseExceptionHandler(BaseException baseEx) {
        return Mono.defer(() -> {
            Exception ex = new ResponseStatusException(HttpStatus.valueOf(baseEx.getTaskStatus().value()), baseEx.getMessage());
            return Mono.error(ex);
        });
    }

}
