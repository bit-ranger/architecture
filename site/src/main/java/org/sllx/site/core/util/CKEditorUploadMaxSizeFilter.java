package org.sllx.site.core.util;

import org.sllx.core.util.NumberUtils;

import javax.servlet.*;
import java.io.IOException;

public class CKEditorUploadMaxSizeFilter implements Filter{


    private int maxUploadSize;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String size = filterConfig.getInitParameter(StaticResourceHolder.MAX_SIZE_PARAM);
        maxUploadSize = NumberUtils.toInt(size, -1);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        int len = request.getContentLength();
        if(maxUploadSize > 0 && maxUploadSize < len){
            String script = String.format("<script type=\"text/javascript\">window.alert(\"文件应小于%sKB\");</script>", maxUploadSize / 1024);
            response.getWriter().print(script);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
