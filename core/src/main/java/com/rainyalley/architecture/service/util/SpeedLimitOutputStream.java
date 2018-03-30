package com.rainyalley.architecture.service.util;

import java.io.*;

/**
 * 限速的输入流
 * @author bin.zhang
 */
public class SpeedLimitOutputStream extends FilterOutputStream {

    private long lastReadTime = -1;

    private int bytePms;


    /**
     * @param in 输入流
     * @param bytePms 每毫秒写入的字节数
     */
    protected SpeedLimitOutputStream(OutputStream in, int bytePms) {
        super(in);
        this.bytePms = bytePms;
    }


    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    /**
     * 以10毫秒的间歇休眠
     * 每次写入时计算两次写入时间间隔
     * 依据限速值，计算出当次需要写入的字节数
     * @param b
     * @param off
     * @param len
     * @return
     * @throws IOException
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        //已写入字节数
        int trn = 0;
        while (trn < len){
            long curr = System.currentTimeMillis();
            if(lastReadTime == -1){
                lastReadTime = curr - 10;
            }

            //时间间隔
            int gap = (int)(curr - lastReadTime);

            int grn = bytePms * gap;

            //当次需要写入的字节数
            int rrn = Math.min(grn, len - trn);

            super.write(b, off + trn, rrn);

            trn += rrn;
            lastReadTime = curr;

            sleep();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            //do nothing
        }
    }
}
