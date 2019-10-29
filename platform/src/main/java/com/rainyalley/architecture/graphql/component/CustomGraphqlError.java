package com.rainyalley.architecture.graphql.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class CustomGraphqlError implements GraphQLError {

    private final int code;
    private final String message;
    private final List<Object> path;
    private final Throwable exception;
    private final List<SourceLocation> locations;
    private ErrorType errorType;

    public CustomGraphqlError(ErrorType errorType,
                              int code,
                              String message,
                              Throwable exception,
                              List<Object> path,
                              List<SourceLocation> locations) {
        this.errorType = errorType;
        this.code = code;
        this.message = message;
        this.path = path;
        this.exception = exception;
        this.locations = locations;
    }

    public int getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<Object> getPath() {
        return path;
    }

    @JsonIgnore
    public Throwable getException() {
        return exception;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return locations;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }
}
