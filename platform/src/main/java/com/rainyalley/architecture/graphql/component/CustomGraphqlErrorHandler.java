package com.rainyalley.architecture.graphql.component;

import com.rainyalley.architecture.exception.TaskStatus;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.ErrorType.*;

@Slf4j
public class CustomGraphqlErrorHandler implements GraphQLErrorHandler {

    private Map<ErrorType, TaskStatus> errorTypeBizStatusHashMap = new HashMap<>();

    public CustomGraphqlErrorHandler() {
        this.errorTypeBizStatusHashMap.put(InvalidSyntax, TaskStatus.BAD_REQUEST);
        this.errorTypeBizStatusHashMap.put(ValidationError, TaskStatus.BAD_REQUEST);
        this.errorTypeBizStatusHashMap.put(DataFetchingException, TaskStatus.INTERNAL_SERVER_ERROR);
        this.errorTypeBizStatusHashMap.put(MutationNotSupported, TaskStatus.NOT_IMPLEMENTED);
        this.errorTypeBizStatusHashMap.put(ExecutionAborted, TaskStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream().map(p -> p instanceof CustomGraphqlError ? p : convert(p)).collect(Collectors.toList());
    }

    private CustomGraphqlError convert(GraphQLError error){
        TaskStatus bizStatus = errorTypeBizStatusHashMap.get(error.getErrorType());
        bizStatus = bizStatus != null? bizStatus:TaskStatus.INTERNAL_SERVER_ERROR;

        return new CustomGraphqlError(
                error.getErrorType(),
                bizStatus.value(),
                error.getMessage(),
                null,
                error.getPath(),
                error.getLocations());
    }
}
