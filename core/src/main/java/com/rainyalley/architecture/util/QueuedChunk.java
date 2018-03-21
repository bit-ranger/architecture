package com.rainyalley.architecture.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class QueuedChunk extends Chunk {

    private BufferedReader br;

    private String head;

    /**
     * io缓冲大小
     */
    private static final int FILE_BUFFER_SIZE = 1024 * 1024;

    public QueuedChunk(Chunk chunk) {
        super(chunk.getLevel(), chunk.getIdx(), chunk.getSize(), chunk.getFile());
        try {
            br = new BufferedReader(new FileReader(getFile()), FILE_BUFFER_SIZE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String peekHead() throws IOException{
        if(head != null){
            return head;
        } else {
            head = br.readLine();
            return head;
        }
    }

    public String takeHead() throws IOException{
        if(head != null){
            String taked = head;
            head = null;
            return taked;
        } else {
            return br.readLine();
        }
    }

}
