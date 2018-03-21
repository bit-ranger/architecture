package com.rainyalley.architecture.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;

public class FileSorter<T> {

    /**
     * 归并路数，必须大于等于2
     */
    private static final int MERGE_WAY_NUM = 8;

    /**
     * io缓冲大小
     */
    private static final int FILE_BUFFER_SIZE = 1024 * 1024 * 2;

    /**
     * 每个块的行数
     */
    private static final int CHUNK_SIZE = 10000;

    private static final String TMP_DIR = "/var/tmp/fileCompare/";

    private Comparator<T> comparator;

    private LineMapper<T> lineMapper;


    public FileSorter(Comparator<T> comparator, LineMapper<T> lineMapper) {
        this.comparator = comparator;
        this.lineMapper = lineMapper;
    }

    private void sort(File file) throws IOException {
        Queue<Chunk> chunkQueue = new LinkedList<>(split(file));
        int currentMergeLevel = 1;


        while (true){
            List<Chunk> pollChunks =  pollChunks(chunkQueue, currentMergeLevel);
            if(CollectionUtils.isEmpty(pollChunks)){
                currentMergeLevel++;
                continue;
            }

            //合并
            Chunk mergedChunk = merge(pollChunks);

            //chunkQueue 中没有后续的元素，表示此次合并是最终合并
            if(chunkQueue.size() == 0){
                chunkQueue.add(mergedChunk);
                break;
            } else {
                chunkQueue.add(mergedChunk);
            }
        }

        Chunk finalChunk = chunkQueue.peek();
        System.out.println(String.format("final chunk: %s", finalChunk.toString()));
    }

    private Chunk merge(List<Chunk> chunks){
        if(chunks.size() == 1){
            return chunks.get(0);
        }
        //~todo~
        return null;
    }

    /**
     * 弹出chunk列表
     * @param chunkQueue 全局chunkQueue
     * @param level 弹出的trunk级别
     * @return
     */
    private List<Chunk> pollChunks(Queue<Chunk> chunkQueue, int level){
        List<Chunk> pollChunks = new ArrayList<>(MERGE_WAY_NUM);
        for (int i = 0; i < MERGE_WAY_NUM; i++) {
            Chunk head = chunkQueue.peek();
            if(head == null){
                break;
            }
            if(head.getLevel() == level){
                pollChunks.add(chunkQueue.poll());
            } else if(head.getLevel() > level){
                break;
            }
        }
        return pollChunks;
    }

    /**
     * 切割
     * @param file 被切割的文件
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private List<Chunk> split(File file) throws IOException{
        if(!file.exists()){
            throw new FileNotFoundException(String.format("file not fond %s", file.getPath()));
        }

        List<Chunk> chunkList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file), FILE_BUFFER_SIZE)){
            int idx = 0;
            String line = null;
            List<T> chunkRows = new ArrayList<>(CHUNK_SIZE);

            while (true){
                line = br.readLine();

                if(line != null){
                    T row = lineMapper.map(line, idx + chunkRows.size());
                    chunkRows.add(row);
                }

                if(line == null || chunkRows.size() >= CHUNK_SIZE){
                    chunkRows.sort(comparator);
                    Chunk chunk = makeChunk(idx, chunkRows, file);
                    chunkList.add(chunk);
                    chunkRows.clear();
                    idx += chunkRows.size();
                }

                if(line == null){
                    break;
                }
            }
        }

        return chunkList;
    }

    /**
     * 创建一个块
     * @param idx 块首行的索引号
     * @param chunkRows 块中的行数
     * @param originalFile 原始文件
     * @return
     * @throws IOException
     */
    private Chunk makeChunk(int idx, List<T> chunkRows, File originalFile) throws IOException{
        File chunkFile = new File(String.format(TMP_DIR + "/%s/%s_%s.txt", originalFile.getName(), idx, chunkRows).replace("/", File.separator));
        FileUtils.deleteQuietly(chunkFile);
        Assert.isTrue(chunkFile.createNewFile(), String.format("createNewFile failure: %s", chunkFile.getPath()));
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(chunkFile), FILE_BUFFER_SIZE)){
            for (T chunkRow : chunkRows) {
                String line = lineMapper.aggregate(chunkRow);
                bw.write(line);
                bw.newLine();
            }
        }

        return new Chunk(1, idx, chunkRows.size());
    }
}
