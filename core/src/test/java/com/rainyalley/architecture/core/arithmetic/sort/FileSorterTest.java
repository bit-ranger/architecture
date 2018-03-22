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
        int numbers = 1000000;
        File file = new File("/var/sort/architecture_user.csv");
        File dest = new File("/var/sort/architecture_user.sorted.csv");
        file.getParentFile().mkdirs();

        List<String> memoryList = new ArrayList<>(numbers);


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for (int i = 0; i < numbers; i++) {
                long id = radom.nextLong();
                String line = id + ",中文名字A,中文密码,1";
                memoryList.add(line);
                bw.write(line);
                bw.newLine();
            }
        }

        FileSorter sorter = new FileSorter((p,n) -> p.compareTo(n),
                8, 10000, 1024*1024*2, new File("/var/tmp/fileSorter"), false);
        sorter.sort(file, dest);

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