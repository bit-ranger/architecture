package com.rainyalley.architecture.graphql.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainyalley.architecture.exception.BaseException;
import com.rainyalley.architecture.exception.ClientException;
import com.rainyalley.architecture.exception.TaskStatus;
import graphql.ErrorType;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author bin.zhang
 */
@Slf4j
public class CustomDataFetcherExceptionHandler implements DataFetcherExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public void accept(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        SourceLocation sourceLocation = handlerParameters.getField().getSourceLocation();
        ExecutionPath path = handlerParameters.getPath();

        CustomGraphqlError error;

        if (exception instanceof AuthenticationException) {
            SecurityContextHolder.getContext().setAuthentication(null);
            error = new CustomGraphqlError(
                    ErrorType.ValidationError,
                    TaskStatus.UNAUTHORIZED.value(),
                    exception.getMessage(),
                    exception,
                    path.toList(),
                    Collections.singletonList(sourceLocation));
        }
        else if (exception instanceof AccessDeniedException) {
            SecurityContextHolder.getContext().setAuthentication(null);
            error = new CustomGraphqlError(
                    ErrorType.ValidationError,
                    TaskStatus.FORBIDDEN.value(),
                    exception.getMessage(),
                    exception,
                    path.toList(),
                    Collections.singletonList(sourceLocation));
        }
        else if(exception instanceof ConstraintViolationException){
            error = new CustomGraphqlError(
                    ErrorType.ValidationError,
                    TaskStatus.BAD_REQUEST.value(),
                    constraintViolationExceptionMessage((ConstraintViolationException)exception),
                    exception,
                    path.toList(),
                    Collections.singletonList(sourceLocation));
        } else if(exception instanceof ClientException){
            error = new CustomGraphqlError(
                    ErrorType.ValidationError,
                    ((ClientException) exception).getTaskStatus().value(),
                    exception.getMessage(),
                    exception,
                    path.toList(),
                    Collections.singletonList(sourceLocation));
        }  else if (exception instanceof BaseException){
            error = new CustomGraphqlError(
                    ErrorType.DataFetchingException,
                    ((BaseException) exception).getTaskStatus().value(),
                    exception.getMessage(),
                    exception,
                    path.toList(),
                    Collections.singletonList(sourceLocation));
            try {
                log.error(objectMapper.writeValueAsString(handlerParameters), exception);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            error = new CustomGraphqlError(
                    ErrorType.DataFetchingException,
                    TaskStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage(),
                    exception,
                    path.toList(),
                    Collections.singletonList(sourceLocation));
            try {
                log.error(objectMapper.writeValueAsString(handlerParameters), exception);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }

        handlerParameters.getExecutionContext().addError(error, handlerParameters.getPath());
    }


    private String constraintViolationExceptionMessage(ConstraintViolationException ex){
        ConstraintViolation violation= ex.getConstraintViolations().stream().findFirst().get();
        Path.Node last = getLastElement(violation.getPropertyPath());
        return last.getName() + " " + violation.getMessage();
    }

    private static <T> T getLastElement(final Iterable<T> elements) {
        final Iterator<T> itr = elements.iterator();
        T lastElement = itr.next();
        while(itr.hasNext()) {
            lastElement=itr.next();
        }
        return lastElement;
    }
}
