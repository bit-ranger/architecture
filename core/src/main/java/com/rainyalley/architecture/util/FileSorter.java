package com.rainyalley.architecture.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class FileSorter {



    /**
     * 初始化块级别
     */
    private static final int INITIAL_CHUNK_LEVEL = 1;

    /**
     * io缓冲大小
     */
    private int ioBufferSize = 1024 * 1024 * 2;


    /**
     * 临时目录
     */
    private String tmpDir = "/var/tmp/fileCompare/";

    /**
     * 行内容比较器
     */
    private Comparator<String> comparator;

    /**
     * 归并路数，必须大于或等于2
     */
    private int mergeWayNum = 8;

    /**
     * 每个块的行数
     */
    private int initialChunkSize = 10000;


    public FileSorter(Comparator<String> comparator) {
        this.comparator = comparator;
    }

    public void sort(File file) throws IOException {
        List<Chunk> splitChunkList = split(file);

        Queue<Chunk> chunkQueue = new LinkedList<>(splitChunkList);
        int currentLevel = INITIAL_CHUNK_LEVEL;

        while (true){
            List<Chunk> pollChunks =  pollChunks(chunkQueue, currentLevel);
            if(CollectionUtils.isEmpty(pollChunks)){
                currentLevel++;
                continue;
            }

            //合并
            Chunk mergedChunk = merge(pollChunks, file);

            //chunkQueue 中没有后续的元素，表示此次合并是最终合并
            if(chunkQueue.size() == 0){
                chunkQueue.add(mergedChunk);
                break;
            } else {
                chunkQueue.add(mergedChunk);
            }
        }

        Chunk finalChunk = chunkQueue.poll();
        File finalChunkFile = chunkFile(finalChunk, file);
        System.out.println(String.format("final chunk: %s", finalChunk.toString()));
    }

    private Chunk merge(List<Chunk> chunks, File originalFile) throws IOException{
        if(chunks.size() == 1){
            return chunks.get(0);
        }

        Stream<Chunk> cs = chunks.stream();
        Integer mergeSize = cs.map(Chunk::getSize).reduce((p, n) -> p + n).get();
        Chunk mergedChunk = new Chunk(chunks.get(0).getLevel() + 1, chunks.get(0).getIdx(), mergeSize);
        File mergedChunkFile = chunkFile(mergedChunk, originalFile);

        try(BufferedWriter mergedChunkFileWriter = new BufferedWriter(new FileWriter(mergedChunkFile))){
            Stream<QueuedLineReader> qlrs = chunks.stream().map(c -> {
                try {
                    return new QueuedLineReader(new BufferedReader(new FileReader(chunkFile(c, originalFile))));
                } catch (FileNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            });

            while (true){
                Optional<QueuedLineReader> minReader = qlrs
                        .filter(p -> p.peek() != null)
                        .min((p,n) -> comparator.compare(p.peek(), n.peek())
                );
                //所有reader都读完了
                if(!minReader.isPresent()){
                    break;
                }

                String line = minReader.get().take();
                mergedChunkFileWriter.write(line);
                mergedChunkFileWriter.newLine();
            }
        }

        return mergedChunk;
    }



    /**
     * 弹出chunk列表
     * @param chunkQueue 全局chunkQueue
     * @param level 弹出的trunk级别
     * @return
     */
    private List<Chunk> pollChunks(Queue<Chunk> chunkQueue, int level){
        List<Chunk> pollChunks = new ArrayList<>(mergeWayNum);
        for (int i = 0; i < mergeWayNum; i++) {
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

        try(BufferedReader br = new BufferedReader(new FileReader(file), ioBufferSize)){
            int idx = 0;
            String line = null;
            List<String> chunkRows = new ArrayList<>(initialChunkSize);

            while (true){
                line = br.readLine();

                if(line != null){
                    chunkRows.add(line);
                }

                if(line == null || chunkRows.size() >= initialChunkSize){
                    chunkRows.sort(comparator);
                    Chunk chunk = initialChunk(idx, chunkRows, file);
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
    private Chunk initialChunk(int idx, List<String> chunkRows, File originalFile) throws IOException{
        Chunk chunk = new Chunk(INITIAL_CHUNK_LEVEL, idx, chunkRows.size());
        File chunkFile = chunkFile(chunk, originalFile);
        FileUtils.deleteQuietly(chunkFile);
        Assert.isTrue(chunkFile.createNewFile(), String.format("createNewFile failure: %s", chunkFile.getPath()));
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(chunkFile), ioBufferSize)){
            for (String chunkRow : chunkRows) {
                bw.write(chunkRow);
                bw.newLine();
            }
        }
        return chunk;
    }

    private File chunkFile(Chunk chunk, File originalFile){
        return new File(String.format(tmpDir + "/%s/%s_%s_%s.txt", originalFile.getName(), chunk.getLevel(), chunk.getIdx(), chunk.getSize()).replace("/", File.separator));
    }

}
