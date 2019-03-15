package com.rainyalley.architecture.graphql.component;

import com.huifu.devops.biz.exception.BizStatus;
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

    private Map<ErrorType,BizStatus> errorTypeBizStatusHashMap = new HashMap<>();

    public CustomGraphqlErrorHandler() {
        this.errorTypeBizStatusHashMap.put(InvalidSyntax, BizStatus.BAD_REQUEST);
        this.errorTypeBizStatusHashMap.put(ValidationError, BizStatus.BAD_REQUEST);
        this.errorTypeBizStatusHashMap.put(DataFetchingException, BizStatus.INTERNAL_SERVER_ERROR);
        this.errorTypeBizStatusHashMap.put(MutationNotSupported, BizStatus.NOT_IMPLEMENTED);
        this.errorTypeBizStatusHashMap.put(ExecutionAborted, BizStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream().map(p -> p instanceof CustomGraphqlError ? p : convert(p)).collect(Collectors.toList());
    }

    private CustomGraphqlError convert(GraphQLError error){
        BizStatus bizStatus = errorTypeBizStatusHashMap.get(error.getErrorType());
        bizStatus = bizStatus != null? bizStatus:BizStatus.INTERNAL_SERVER_ERROR;

        return new CustomGraphqlError(
                error.getErrorType(),
                bizStatus.value(),
                error.getMessage(),
                null,
                error.getPath(),
                error.getLocations());
    }
}
