package com.rainyalley.architecture.arithmetic.sort;

import com.rainyalley.architecture.util.Assert;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author bin.zhang
 */
public class FileSorter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSorter.class);

    /**
     * 初始化块级别
     */
    private static final int INITIAL_CHUNK_LEVEL = 1;

    /**
     * 第一行的行号
     */
    private static final int INITIAL_ROW_NUM = 1;

    /**
     * io缓冲大小
     */
    private int ioBufferSize;

    /**
     * 行内容比较器
     */
    private Comparator<String> comparator;

    /**
     * 归并路数，必须大于或等于2
     */
    private int mergeWayNum;

    /**
     * 内存中排序的行数
     */
    private int inMemSize;

    /**
     * 排序完成后是否删除临时文件
     */
    private boolean clean;

    /**
     * 阻塞队列容量100
     * 最大空闲时间10秒
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * @param comparator   行比较器
     * @param mergeWayNum  归并路数
     * @param inMemRowNum  内存中排序的行数
     * @param ioBufferSize io缓冲区容量
     * @param clean        排序完成后，是否清除临时文件
     */
    public FileSorter(Comparator<String> comparator, ThreadPoolExecutor threadPoolExecutor, int mergeWayNum, int inMemRowNum, int ioBufferSize, boolean clean) {
        Assert.isTrue(mergeWayNum >= 2, "mergeWayNum must greater than 2");
        Assert.isTrue(inMemRowNum >= 1, "inMemSize must greater than 1");
        Assert.isTrue(ioBufferSize >= 1024, "ioBufferSize must greater than 1024");

        this.comparator = comparator;
        this.mergeWayNum = mergeWayNum;
        this.inMemSize = inMemRowNum;
        this.ioBufferSize = ioBufferSize;
        this.clean = clean;

        //并发归并线程池，队列容量为归并路数
        this.threadPoolExecutor = threadPoolExecutor;
    }


    /**
     * 排序
     *
     * @param src 原文件
     * @param dst 排序后存文件的目录
     * @throws IOException
     */
    public void sort(File src, File dst) throws IOException {
        beforeSort(src, dst);

        File workDir = workDir(dst);

        //切割
        List<Chunk> splitChunkList = split(src, workDir);

        Chunk finalChunk = null;
        if(splitChunkList.size() > 1){
            finalChunk = mergeAll(splitChunkList, workDir);
        } else if(splitChunkList.size() == 1){
            finalChunk = splitChunkList.get(0);
        } else {
            throw new IllegalArgumentException("splitChunkList.size == 0");
        }
        finalChunk.close();
        FileUtils.moveFile(finalChunk.getChunkFile(), dst);
        afterSort(src, dst);
    }

    private Chunk mergeAll(List<Chunk> splitChunkList, File workDir){
        //块队列，将从此队列中不断取出块，合并后放入此队列
        Queue<Chunk> chunkQueue = new LinkedList<>(splitChunkList);

        //当前处理的chunk level
        int currentLevel = INITIAL_CHUNK_LEVEL;

        List<Future<Chunk>> mergeFutureList = new ArrayList<>();
        while (true) {
            //从队列中获取一组chunk
            List<Chunk> pollChunks = pollChunks(chunkQueue, currentLevel);

            //未取到同级chunk, 表示此级别已合并完成
            if (pollChunks == null || pollChunks.isEmpty()) {
                mergeFutureList.stream().map(this::get).forEach(chunkQueue::add);
                mergeFutureList.clear();
                //chunkQueue 中只有一个元素，表示此次合并是最终合并
                if (chunkQueue.size() == 1) {
                    break;
                } else {
                    currentLevel++;
                    continue;
                }
            }

            Future<Chunk> chunk = threadPoolExecutor.submit(() -> merge(pollChunks, workDir));
            mergeFutureList.add(chunk);
        }

        return chunkQueue.poll();
    }

    private Chunk get(Future<Chunk> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void beforeSort(File src, File dst) throws IOException {
        Assert.isTrue(src.exists(), String.format("src file not fond: %s", src.getPath()));
        Assert.isTrue(!dst.exists(), String.format("dst file must not exist: %s", dst.getPath()));

        FileUtils.cleanDirectory(workDir(dst));
    }

    private void afterSort(File src, File dst) throws IOException {
        if (clean){
            FileUtils.deleteQuietly(workDir(dst));
        }
    }

    /**
     * 合并
     *
     * @param chunks
     * @return
     * @throws IOException
     */
    private Chunk merge(List<Chunk> chunks, File workDir) throws IOException {
        Chunk mergedChunk = new Chunk(chunks, comparator);
        mergedChunk.store(workDir, ioBufferSize);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("merge {}, {}", chunks.size(), chunks);
        }
        for (Chunk chunk : chunks) {
            chunk.close();
        }
        return mergedChunk;
    }



    /**
     * 弹出chunk列表
     *
     * @param chunkQueue 全局chunkQueue
     * @param level      弹出的trunk级别
     * @return
     */
    private List<Chunk> pollChunks(Queue<Chunk> chunkQueue, int level) {
        List<Chunk> pollChunks = new ArrayList<>(mergeWayNum);
        for (int i = 0; i < mergeWayNum; i++) {
            Chunk head = chunkQueue.peek();
            if (head == null) {
                break;
            }
            if (head.getLevel() == level) {
                pollChunks.add(chunkQueue.poll());
            } else if (head.getLevel() > level) {
                break;
            }
        }
        return pollChunks;
    }

    /**
     * 切割
     *
     * @param src 被切割的文件
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private List<Chunk> split(File src, File workDir) throws IOException {

        List<Chunk> chunkList;

        try (BufferedReader br = new BufferedReader(new FileReader(src), ioBufferSize)) {
            int rowNum = INITIAL_ROW_NUM;
            String line;
            List<String> chunkRows = new ArrayList<>(inMemSize);

            List<Future<Chunk>> sortFutureList = new ArrayList<>();
            List<Future<Chunk>> storeFutureList = new ArrayList<>();
            while (true) {
                line = br.readLine();

                if (line != null) {
                    chunkRows.add(line);
                }

                if (line == null || chunkRows.size() >= inMemSize) {
                    if (chunkRows.size() > 0) {
                        final int rn = rowNum;
                        final List<String> cr = chunkRows;

                        rowNum += chunkRows.size();
                        chunkRows = new ArrayList<>();

                        Future<Chunk> sort = threadPoolExecutor.submit(() -> {
                            cr.sort(comparator);
                            Chunk chunk = new Chunk(INITIAL_CHUNK_LEVEL, rn, cr);
                            Future<Chunk> store = threadPoolExecutor.submit(() -> chunk.store(workDir, ioBufferSize));
                            storeFutureList.add(store);
                            return chunk;
                        });
                        sortFutureList.add(sort);
                    }
                }

                if (line == null) {
                    break;
                }
            }
            chunkList = sortFutureList.stream().map(this::get).collect(Collectors.toList());
            storeFutureList.forEach(this::get);
        }
        return chunkList;
    }

    private File workDir(File dst){
        File work = new File(dst.getPath() + ".tmp");
        if(!work.exists()){
            work.mkdirs();
        }
        return work;
    }
}
