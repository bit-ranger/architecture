package com.rainyalley.architecture.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class ServletFilter implements Filter {

    private HttpServlet httpServlet;

    public ServletFilter(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        httpServlet.service(request, response);
    }

    @Override
    public void destroy() {

    }


}
