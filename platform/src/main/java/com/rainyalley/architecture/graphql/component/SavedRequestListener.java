package com.rainyalley.architecture.graphql.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huifu.devops.application.component.IpAddressUtil;
import com.huifu.devops.application.component.SavedRequestBody;
import com.huifu.devops.application.component.security.SecurityContext;
import com.huifu.devops.biz.exception.BizStatus;
import com.huifu.devops.biz.model.SavedRequest;
import com.huifu.devops.biz.service.SavedRequestService;
import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLServletListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class SavedRequestListener implements GraphQLServletListener {

    private static final String URI_PREFIX = "/graphql";

    private ObjectMapper objectMapper = new ObjectMapper();
    private PortResolver portResolver = new PortResolverImpl();
    private SavedRequestService savedRequestService;
    private SecurityContext securityContext;
    private RequestMatcher saveRequestMatcher = new AntPathRequestMatcher("/**", HttpMethod.POST.name());
    private List<String> ignorePathPrefix = Arrays.asList("__schema");


    public SavedRequestListener(SavedRequestService savedRequestService, SecurityContext securityContext) {
        this.savedRequestService = savedRequestService;
        this.securityContext = securityContext;
    }

    @Override
    public OperationCallback onOperation(GraphQLContext context, String operationName, String query, Map<String, Object> variables) {
        return new OperationCallback() {

            @Override
            public void onSuccess(GraphQLContext context, String operationName, String query, Map<String, Object> variables, Object data) {
                if(!context.getRequest().isPresent()){
                    return;
                }
                HttpServletRequest request = context.getRequest().get();
                if(!saveRequestMatcher.matches(request)){
                    return;
                }
                String path = String.valueOf(request.getAttribute(SavedRequestInstrumentation.PATH_ATTR_NAME));
                if(ignorePathPrefix.stream().anyMatch(path::startsWith)){
                    return;
                }


                try {
                    DefaultSavedRequest savedRequestBody = new SavedRequestBody(request, portResolver);
                    String body = objectMapper.writeValueAsString(savedRequestBody);
                    SavedRequest savedRequest = new SavedRequest();
                    savedRequest.setUri(URI_PREFIX + "/" + path);
                    savedRequest.setMethod(savedRequestBody.getMethod());
                    savedRequest.setBody(body);
                    savedRequest.setCreateUser(securityContext.getUser(request).getUserId());
                    savedRequest.setClientAddr(IpAddressUtil.getIpAddress(request));
                    savedRequest.setStartTime(new Date());
                    savedRequest.setStatus(BizStatus.OK.value());
                    savedRequest.setEndTime(new Date());
                    savedRequestService.save(savedRequest);
                } catch (Throwable e) {
                    log.error("onSuccess error", e);
                }
            }
        };
    }


}
