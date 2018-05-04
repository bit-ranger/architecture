package com.rainyalley.architecture.filter.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainyalley.architecture.exception.BadRequestException;
import com.rainyalley.architecture.vo.ErrorVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * XSS拦截器
 * @author bin.zhang
 */
public class XssFilter implements Filter {

    private  XssChecker xssChecker = new SimpleXssChecker();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("XssFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            doFilterInternal(httpRequest, httpResponse, chain);
        } catch (BadRequestException e){
            ErrorVo info = new ErrorVo();
            info.setMessage(e.getMessage());
            info.setCode(HttpStatus.BAD_REQUEST.value());
            String result = new ObjectMapper().writeValueAsString(info);
            httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(result);
            response.getWriter().flush();
        }

    }

    private void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException{
        if(!xssChecker.isValid(httpRequest.getRequestURI())){
            throw new BadRequestException("The uri string contains invalid tag");
        }

        if(HttpMethod.GET.name().equalsIgnoreCase(httpRequest.getMethod())){
            if(!xssChecker.isValid(httpRequest.getQueryString())){
                throw new BadRequestException("The query string contains invalid tag");
            }
        }

        String contentType = httpRequest.getContentType();
        if(HttpMethod.POST.name().equalsIgnoreCase(httpRequest.getMethod()) || HttpMethod.PUT.name().equalsIgnoreCase(httpRequest.getMethod())){
            if(StringUtils.contains(contentType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)){
                for (Map.Entry<String,String[]> entry : httpRequest.getParameterMap().entrySet()) {
                    for (String s : entry.getValue()) {
                        if(!xssChecker.isValid(s)){
                            throw new BadRequestException(String.format("The parameter %s contains invalid tag", entry.getKey()));
                        }
                    }
                }
            }

        }

        if(StringUtils.contains(contentType, MediaType.MULTIPART_FORM_DATA_VALUE)){
            chain.doFilter(httpRequest, httpResponse);
        } else {
            chain.doFilter(new XssRequestWrapper(httpRequest, xssChecker), httpResponse);
        }
    }

    @Override
    public void destroy() {}
}
