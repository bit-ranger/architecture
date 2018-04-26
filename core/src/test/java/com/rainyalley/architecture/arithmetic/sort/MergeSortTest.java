package com.rainyalley.architecture.arithmetic.sort;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MergeSortTest {

    @Test
    public void sort() throws Exception {
        List<Integer> src = new ArrayList<>();
        int num = Double.valueOf(Math.random() * 10000).intValue();
        for (int i = 0; i < num; i++) {
            src.add(Double.valueOf(Math.random()*num).intValue());
        }

        MergeSort sort = new MergeSort();

        Integer arrMS[] = new Integer[src.size()];
        sort.sort(src.toArray(arrMS), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        Collections.sort(src);
        Integer arrCS[] = new Integer[src.size()];
        src.toArray(arrCS);

        Assert.assertArrayEquals(arrMS, arrCS);
    }

}