package com.rainyalley.architecture.core.arithmetic.sort;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ExternalMergeSortTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMergeSortTest.class);

    @Test
    public void sort() throws Exception {

        Charset charset = Charset.forName("UTF-8");
        Random radom = new Random();

        LOGGER.debug("fill value");
        File file = new File("/var/sort/architecture_user.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 10000; i++) {
            long id = radom.nextLong();
            bw.write( id + ",中文名字A,中文密码,1");
            bw.newLine();
        }
        bw.flush();
        bw.close();

        LOGGER.debug("findSizeAndMaxLineWidth");
        BufferedReader br = new BufferedReader(new FileReader(file));
        final Pair<Long,Integer> pair = CsvByteDataConverter.findSizeAndMaxLineWidth(br, charset);
        br.close();


        LOGGER.debug("set FileStore");
        FileStore<CsvRow> ies = new FileStore<>("/var/sort/architecture_user.es", pair.getLeft(), new CsvByteDataConverter(pair.getRight(), charset));
        br = new BufferedReader(new FileReader(file));
        try {
            int index = 0;
            String line = null;
            while ((line = br.readLine()) != null){
                CsvRow csvRow = new CsvRow(line);
                ies.set(index, csvRow);
                index++;
            }
            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(br);
        }

        try {

            LOGGER.debug("sort");
            ExternalMergeSort sort = new ExternalMergeSort();
            sort.sort(ies);


            LOGGER.debug("set ArrayList");
            ArrayList<CsvRow> al = new ArrayList();
            br = new BufferedReader(new FileReader(file));
            try {
                String line = null;
                while ((line = br.readLine()) != null){
                    CsvRow csvRow = new CsvRow(line);
                    al.add(csvRow);
                }
                br.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(br);
            }

            LOGGER.debug("sort ArrayList");
            Collections.sort(al);

            LOGGER.debug("assert");
            ies.flush();
            for (int i = 0; i < pair.getLeft(); i++) {
                Assert.isTrue(ies.get(i).compareTo(al.get(i))  == 0);
            }

            LOGGER.debug("complete");
        } finally {
            ies.close();
        }
    }

}