package com.rainyalley.architecture.core.arithmetic.sort;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CachedStoreTest {
    private static Charset charset = Charset.forName("UTF-8");

    private static int numbers = 10000;

    private static CachedStore<CsvRow> fs;


    @BeforeClass
    public static void before() throws Exception{

        Random radom = new Random();

        File file = new File("/var/sort/architecture_user.csv");
        file.getParentFile().mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 10000; i++) {
            long id = radom.nextLong();
            String line = id + ",中文名字A,中文密码,1";
            bw.write(line);
            bw.newLine();
        }
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new FileReader(file));
        final Pair<Long,Integer> pair = CsvByteDataConverter.findSizeAndMaxLineWidth(br, charset);
        br.close();

        FileStore<CsvRow> fileStore = new FileStore<CsvRow>("/var/sort/FileStoreTest", numbers, new CsvByteDataConverter(pair.getRight(), charset));
        fs = new CachedStore<>(fileStore, 100,0, pair.getLeft() - 1);
    }




    //@Test
    public void setAndGet() throws Exception {
        File file = new File("/var/sort/architecture_user.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<CsvRow> al = new ArrayList<>();
        try {
            int index = 0;
            String line = null;
            while ((line = br.readLine()) != null){
                CsvRow csvRow = new CsvRow(line);
                fs.set(index, csvRow);
                al.add(csvRow);
                index++;
            }
            br.close();
            fs.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(br);
        }

        for (int i = 0; i < numbers; i++) {
            Assert.assertEquals(0, al.get(i).compareTo(fs.get(i)));
        }



    }

    @Test
    public void set1AndGet1() throws Exception {
        File file = new File("/var/sort/architecture_user.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<CsvRow> al = new ArrayList<>();
        try {
            String line = null;
            while ((line = br.readLine()) != null){
                CsvRow csvRow = new CsvRow(line);
                al.add(csvRow);
            }
            br.close();

            fs.set(0, al);
            fs.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(br);
        }

        List<CsvRow> alfs = fs.get(0, numbers);

        for (int i = 0; i < numbers; i++) {
            try {
                Assert.assertEquals(0, al.get(i).compareTo(alfs.get(i)));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Test
    public void size() throws Exception {
        Assert.assertEquals(numbers, fs.size());
    }


    private static CachedStore<CsvRow> fsFork = null;

//    @Test
//    public void fork() throws Exception {
//        fsFork = fs.fork(fs.name() + ".fork", fs.size());
//    }




    @AfterClass
    public static void afterClass() throws Exception {
       // fsFork.delete();
        fs.close();
        fs.delete();
        new File("/var/sort/architecture_user.csv").delete();
    }
}