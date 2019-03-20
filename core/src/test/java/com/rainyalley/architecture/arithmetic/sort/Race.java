package com.rainyalley.architecture.arithmetic.sort;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Race implements Closeable{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSorter.class);

    /**
     * io缓冲大小
     */
    private int ioBufferSize = 1024*1024;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.CallerRunsPolicy());


    private int inAreaSortMemRowNum = 50000;

    private int areaColumnIdx = 7;

    private int registerTimeColumnIdx = 9;

    private int ageColumnIdx = 2;

    private boolean clean;


    public static void main(String[] args) throws Exception{
        Race race = new Race();
        race.race(new File("D:\\Download\\testrace.csv"), new File("D:\\Download\\race-result.csv"));
        race.close();
    }

    private void race(File src, File dst) throws Exception{
        File workDir = workDir(dst);
        if(workDir.exists()){
            FileUtils.cleanDirectory(workDir);
        } else {
            FileUtils.forceMkdir(workDir);
        }

        List<Area> grpList = group(src, workDir);

        Comparator<String> inAreaSortComparator = Comparator.comparingInt(l -> Integer.parseInt(StringUtils.split(l, ",")[ageColumnIdx]));
        FileSorter sorter = new FileSorter(inAreaSortComparator, threadPoolExecutor, 2, inAreaSortMemRowNum, ioBufferSize, clean);

        List<Callable<Boolean>> tasks = grpList
                .stream()
                .map(g -> (Callable<Boolean>) () -> {sort(sorter, g); return true;})
                .collect(Collectors.toList());

        threadPoolExecutor.invokeAll(tasks);

        BufferedWriter dstWriter = new BufferedWriter(new FileWriter(dst));
        for (Area group : grpList) {
            try (Stream<String> stream = Files.lines(Paths.get(group.getSorted().getPath()))) {
                stream.forEach(l -> writeLine(dstWriter, l));
            }
        }

        dstWriter.close();

        if(clean){
            FileUtils.deleteQuietly(workDir);
        }
    }

    private void writeLine(BufferedWriter writer, String line){
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void sort(FileSorter sorter, Area g){
        try {
            sorter.sort(g.getFile(), g.getSorted());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<Area> group(File src, File workDir){
        Map<String, Area> nameGrp = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(src.getPath()))) {

            stream.forEach(l -> writeToGrp(l, nameGrp, workDir, ioBufferSize));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Area> grpList =  new ArrayList<>(nameGrp.values());
        try {
            for (Area group : grpList) {
                group.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        grpList.sort(Comparator.comparing(Area::getFirstRegisterTime));
        return grpList;
    }

    private void writeToGrp(String line, Map<String, Area> nameGrp, File workDir, int ioBufferSize){
        try {
            String[] columnArr = StringUtils.split(line,",");
            String area = null;
            String registerTime = null;
            try {
                area = columnArr[areaColumnIdx];
                registerTime = columnArr[registerTimeColumnIdx];
            } catch (Exception e) {
                throw new IllegalArgumentException(line);
            }
            Area grp = nameGrp.get(area);
            if(grp == null){
                grp = new Area(area, workDir, ioBufferSize);
                nameGrp.put(area, grp);
            }
            if(grp.getFirstRegisterTime() == null){
                grp.setFirstRegisterTime(registerTime);
            } else if(registerTime.compareTo(grp.getFirstRegisterTime()) < 0){
                grp.setFirstRegisterTime(registerTime);
            }

            grp.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File workDir(File dst){
        return  new File(dst.getPath() + ".tmp");
    }

    @Override
    public void close() throws IOException {
        threadPoolExecutor.shutdownNow();
    }

    private static class Area implements Closeable {

        private String name;

        private File file;

        private File sorted;

        private String firstRegisterTime;

        private BufferedWriter writer;

        public Area(String name, File workDir, int ioBufferSize) throws IOException{
            this.name = name;
            this.file = new File(workDir + File.separator + name);
            this.sorted = new File(workDir + File.separator + name + ".sorted");
            this.writer = new BufferedWriter(new FileWriter(file), ioBufferSize);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getFirstRegisterTime() {
            return firstRegisterTime;
        }

        public void setFirstRegisterTime(String firstRegisterTime) {
            this.firstRegisterTime = firstRegisterTime;
        }

        public File getSorted() {
            return sorted;
        }

        public void write(String line) throws IOException{
            writer.write(line);
            writer.newLine();
        }

        @Override
        public void close() throws IOException {
            writer.close();
        }
    }
}
