package com.rainyalley.architecture.graphql.component;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.NoOpInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters;
import graphql.servlet.GraphQLContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class SavedRequestInstrumentation extends NoOpInstrumentation {

    public static final String PATH_ATTR_NAME = SavedRequestInstrumentation.class.getName()+".PATH_ATTR_NAME";

    @Override
    public InstrumentationContext<ExecutionResult> beginField(InstrumentationFieldParameters parameters) {
        Optional<HttpServletRequest> request = ((GraphQLContext)parameters.getExecutionContext().getContext()).getRequest();
        if(request.isPresent()){
            Optional<String> path = parameters.getTypeInfo().getPath().toList().stream().map(String::valueOf).reduce((l, r) -> l+"/"+r);
            request.get().setAttribute(PATH_ATTR_NAME, path.orElse("/"));
        }
        return super.beginField(parameters);
    }


}
