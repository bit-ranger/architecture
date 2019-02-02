package com.rainyalley.architecture.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author bin.zhang
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OptionalResultHandler implements HandlerResultHandler {


    private ResponseBodyResultHandler responseBodyResultHandler;

    private WebExceptionHandler webExceptionHandler;

    public OptionalResultHandler(WebExceptionHandler webExceptionHandler, ResponseBodyResultHandler responseBodyResultHandler) {
        this.webExceptionHandler = webExceptionHandler;
        this.responseBodyResultHandler = responseBodyResultHandler;
    }

    @Override
    public boolean supports(HandlerResult result) {
        return result != null && result.getReturnValue() instanceof Optional;
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Optional<?> returnValue = (Optional)result.getReturnValue();
        if(returnValue != null && returnValue.isPresent()){
            return responseBodyResultHandler.handleResult(exchange,
                    new HandlerResult(result.getHandler(), returnValue.get(), result.getReturnTypeSource()));
        } else {
            return webExceptionHandler.handle(exchange, new ResponseStatusException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
        }
    }
}
