package com.rainyalley.architecture.filter.xss;

import com.rainyalley.architecture.exception.BadRequestException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

class XssCheckServletInputStream extends ServletInputStream {

    private ServletInputStream servletInputStream;

    private byte[] buffer;

    private int defaultBufferSize = 1000;

    private int bufferPadding = 10;

    private int bufferEndPosition = -1;

    private XssChecker xssChecker;

    private Charset charset;

    public XssCheckServletInputStream(ServletInputStream servletInputStream, Charset charset, long contentLength, XssChecker xssChecker) {
        this.servletInputStream = servletInputStream;
        this.xssChecker = xssChecker;
        this.charset = charset;

        int size = defaultBufferSize;
        if(contentLength > 0){
            size = (int) Long.min(contentLength, defaultBufferSize);
        }

        buffer = new byte[size];
    }

    @Override
    public boolean isFinished() {
        return servletInputStream.isFinished();
    }

    @Override
    public boolean isReady() {
        return servletInputStream.isReady();
    }

    @Override
    public void setReadListener(ReadListener listener) {
        servletInputStream.setReadListener(listener);
    }

    @Override
    public int read() throws IOException {
        int last =  servletInputStream.read();
        if(last == -1){
            checkAndResetBuffer();
        } else {
            bufferEndPosition++;
            buffer[bufferEndPosition] = (byte) last;
            if(bufferEndPosition == buffer.length - 1){
                checkAndResetBuffer();
            }
        }

        return last;
    }

    private void checkAndResetBuffer(){
        String str = new String(buffer, 0, bufferEndPosition + 1, charset);
        if(!xssChecker.isValid(str)){
            throw new BadRequestException("The request body contains invalid tag");
        }

        //tail bytes move to head
        System.arraycopy(buffer, buffer.length - bufferPadding, buffer, 0, bufferPadding);
        bufferEndPosition = bufferPadding - 1;
    }
}
