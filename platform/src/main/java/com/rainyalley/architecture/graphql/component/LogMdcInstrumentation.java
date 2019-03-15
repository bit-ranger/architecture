package com.rainyalley.architecture.graphql.component;

import com.rainyalley.architecture.graphql.SecurityContext;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.NoOpInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters;
import graphql.servlet.GraphQLContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class LogMdcInstrumentation extends NoOpInstrumentation {

    private SecurityContext securityContext;

    public LogMdcInstrumentation(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    /**
     * 会话ID
     */
    private final static String USER_ID = "userId";

    @Override
    public InstrumentationContext<ExecutionResult> beginField(InstrumentationFieldParameters parameters) {
        Optional<HttpServletRequest> request = ((GraphQLContext)parameters.getExecutionContext().getContext()).getRequest();
        if(request.isPresent()){
            MDC.put(USER_ID, getUserId(request.get()));
        }
        return super.beginField(parameters);
    }


    private String getUserId(HttpServletRequest request){
        return securityContext.getUser(request).getUserId();
    }
}
