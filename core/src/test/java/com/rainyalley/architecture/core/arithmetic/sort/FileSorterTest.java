package com.rainyalley.architecture.core.arithmetic.sort;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileSorterTest {

    @Test
    public void sort() throws Exception {

        Random radom = new Random();
        int numbers = 100000000;
        File file = new File("/var/sort/architecture_user.csv");
        File dest = new File("/var/sort/architecture_user.sorted.csv");
        file.getParentFile().mkdirs();

        List<String> memoryList = new ArrayList<>(numbers);


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for (int i = 0; i < numbers; i++) {
                long id = radom.nextLong();
                String line = String.valueOf(id);
                memoryList.add(line);
                bw.write(line);
                bw.newLine();
            }
        }

        FileSorter sorter = new FileSorter((p,n) -> p.compareTo(n),
                8, 100000, 1024*1024*2, new File("/var/tmp/fileSorter"), false);
        long start = System.currentTimeMillis();
        sorter.sort(file, dest);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        List<String> diskList   = new ArrayList<>(numbers);
        try(BufferedReader br = new BufferedReader(new FileReader(dest))){
            String dLine = null;
            while ((dLine = br.readLine()) != null){
                diskList.add(dLine);
            }
        }

        memoryList.sort((p,n) -> p.compareTo(n));
        Assert.assertEquals(memoryList, diskList);
    }

}