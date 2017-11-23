package com.rainyalley.architecture.core.arithmetic.sort;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConcurrentExternalMergeSortTest {
    @Test
    public void sort() throws Exception {

        List<Integer> src = new ArrayList<>();
        int num = 10000;
        for (int i = 0; i < num; i++) {
            src.add(Double.valueOf(Math.random()*num).intValue());
        }

        ExternalStore<Integer> ies = new FileExternalStore<Integer>("/var/sort/externalMerge.tmp", src.size(), new IntegerByteData());

        try {
            for (int i=0; i<src.size(); i++) {
                ies.set(i, src.get(i));
            }

            ConcurrentExternalMergeSort sort = new ConcurrentExternalMergeSort();
            sort.sort(ies);

            Collections.sort(src);

            for (int i=0; i<src.size(); i++) {
                Assert.assertEquals(ies.get(i), src.get(i));
            }
        } finally {
            ies.close();
        }


    }

}