package com.rainyalley.architecture.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 限速的输入流
 * @author bin.zhang
 */
public class SpeedLimitInputStream extends FilterInputStream {

    private long lastReadTime = -1;

    private int bytePms;


    /**
     * @param in 输入流
     * @param bytePms 每毫秒读取的字节数
     */
    protected SpeedLimitInputStream(InputStream in, int bytePms) {
        super(in);
        this.bytePms = bytePms;
    }


    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    /**
     * 以10毫秒的间歇休眠
     * 每次读取时计算两次读取时间间隔
     * 依据限速值，计算出当次需要读出的字节数
     * @param b
     * @param off
     * @param len
     * @return
     * @throws IOException
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //已读出字节数
        int trn = 0;
        while (trn < len){
            long curr = System.currentTimeMillis();
            if(lastReadTime == -1){
                lastReadTime = curr - 10;
            }

            //时间间隔
            int gap = (int)(curr - lastReadTime);

            int grn = bytePms * gap;

            //当次需要读出的字节数
            int rrn = Math.min(grn, len - trn);

            int arn = super.read(b, off + trn, rrn);
            //已读光
            if(arn == -1){
                break;
            }

            trn += arn;
            lastReadTime = curr;

            sleep();
        }

        return trn;
    }

    private void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            //do nothing
        }
    }
}
