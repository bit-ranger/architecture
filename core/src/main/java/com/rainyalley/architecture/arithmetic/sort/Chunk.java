package com.rainyalley.architecture.arithmetic.sort;

import java.io.*;
import java.util.*;

/**
 * @author bin.zhang
 */
public class Chunk implements Closeable{

    private File chunkFile;

    private BufferedReader chunkFileReader;

    private int level;

    private int idx;

    private int size;

    private List<String> rows = Collections.emptyList();

    private List<Chunk> parts = Collections.emptyList();

    private Comparator<String> comparator;

    private String head;

    public Chunk(int level, int idx, List<String> rows) {
        this.level = level;
        this.idx = idx;
        this.rows = rows;
        this.size = rows.size();
    }

    public Chunk(List<Chunk> chunks, Comparator<String> comparator){
        this.level = chunks.get(0).getLevel() + 1;
        this.idx = chunks.get(0).getIdx();
        this.size = chunks.stream().map(Chunk::getSize).reduce((p, n) -> p + n).get();
        this.comparator = comparator;
        this.parts = chunks;
    }

    public Chunk store(File workDir, int bufferSize){
        try {
            this.chunkFile =  new File(String.format(workDir.getPath() + File.separator +"%s_%s_%s", level, idx, size));
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(chunkFile), bufferSize)){
                if(!rows.isEmpty()){
                    for (String chunkRow : rows) {
                        bw.write(chunkRow);
                        bw.newLine();
                    }
                    rows = Collections.emptyList();
                }

                if(!parts.isEmpty()){
                    while (true) {
                        List<Chunk> chunkList =  new ArrayList<>(parts);
                        Chunk headMinChunk = peekMinRemoveEmpty(chunkList);
                        //所有chunk都读完了
                        if (headMinChunk == null) {
                            break;
                        }

                        String line = headMinChunk.take();
                        bw.write(line);
                        bw.newLine();
                    }
                    parts = Collections.emptyList();
                }

            }

            this.chunkFileReader = new BufferedReader(new FileReader(chunkFile), bufferSize);
            this.head = chunkFileReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return this;
    }

    private String peek(){
        return head;
    }

    private String take(){
        if(head == null){
            return null;
        }

        String result = head;
        try {
            head = chunkFileReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Chunk peekMinRemoveEmpty(List<Chunk> list) {
        Iterator<Chunk> i = list.iterator();
        Chunk candidate = null;

        while (i.hasNext()) {
            Chunk next = i.next();

            if (next.peek() == null) {
                i.remove();
                continue;
            } else if (candidate == null) {
                candidate = next;
                continue;
            }

            if (comparator.compare(next.peek(), candidate.peek()) < 0) {
                candidate = next;
            }
        }
        return candidate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Chunk{");
        sb.append("level=").append(level);
        sb.append(", idx=").append(idx);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }

    public int getLevel() {
        return level;
    }

    public int getIdx() {
        return idx;
    }

    public int getSize() {
        return size;
    }

    public File getChunkFile() {
        return chunkFile;
    }

    @Override
    public void close() throws IOException {
        chunkFileReader.close();
    }
}
