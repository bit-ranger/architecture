package com.rainyalley.architecture.filter.xss;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.Charset;

class XssRequestWrapper extends HttpServletRequestWrapper {

    private ServletInputStream servletInputStream;

    private XssChecker xssChecker;

    public XssRequestWrapper(HttpServletRequest request, XssChecker xssChecker) {
        super(request);
        this.xssChecker = xssChecker;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(servletInputStream != null){
            return  servletInputStream;
        }
        Charset charset = null;
        if(StringUtils.isBlank(getContentType())){
            MediaType mediaType = MediaType.valueOf(getContentType());
            charset = mediaType.getCharset();
        }

        if(charset == null){
            charset = Charset.forName("UTF-8");
        }

        servletInputStream = new XssCheckServletInputStream(super.getInputStream(), charset, getContentLengthLong(), xssChecker);
        return servletInputStream;
    }
}
