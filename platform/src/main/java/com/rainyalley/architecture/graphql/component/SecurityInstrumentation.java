package com.rainyalley.architecture.graphql.component;

import graphql.ExecutionResult;
import graphql.execution.ExecutionContext;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.NoOpInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters;
import graphql.servlet.GraphQLContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class SecurityInstrumentation extends NoOpInstrumentation {

    private static final String AUTH_ATTR_NAME = SecurityInstrumentation.class.getName() + ".AUTH_ATTR_NAME";

    @Override
    public ExecutionContext instrumentExecutionContext(ExecutionContext executionContext, InstrumentationExecutionParameters parameters) {
        Optional<HttpServletRequest> request = ((GraphQLContext)executionContext.getContext()).getRequest();
        request.ifPresent(httpServletRequest -> httpServletRequest.setAttribute(AUTH_ATTR_NAME, SecurityContextHolder.getContext().getAuthentication()));
        return super.instrumentExecutionContext(executionContext, parameters);
    }


    /**
     * 跨线程传递authentication
     * 避免重复访问db
     */

    @Override
    public InstrumentationContext<ExecutionResult> beginField(InstrumentationFieldParameters parameters) {
        Optional<HttpServletRequest> request = ((GraphQLContext)parameters.getExecutionContext().getContext()).getRequest();
        if(request.isPresent()){
            Authentication auth = (Authentication)request.get().getAttribute(AUTH_ATTR_NAME);
            if(auth!=null){
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return super.beginField(parameters);
    }


}
