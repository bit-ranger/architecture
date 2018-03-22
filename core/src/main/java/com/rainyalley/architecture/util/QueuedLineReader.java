package com.rainyalley.architecture.util;

import java.io.BufferedReader;
import java.io.IOException;

public class QueuedLineReader{

    private BufferedReader br;

    private String head;

    public QueuedLineReader(BufferedReader reader) {
        br = reader;
    }


    public void close() throws IOException{
        if(br == null){
            return;
        }
        br.close();
        br = null;
    }

    public String peek(){
        if(head != null){
            return head;
        } else {
            try {
                head = br.readLine();
                return head;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String take(){
        if(head != null){
            String taked = head;
            head = null;
            return taked;
        } else {
            try {
                return br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
