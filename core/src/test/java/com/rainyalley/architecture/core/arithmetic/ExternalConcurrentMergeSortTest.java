package com.rainyalley.architecture.core.arithmetic;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalConcurrentMergeSortTest {
    @Test
    public void sort() throws Exception {

        List<Integer> src = new ArrayList<>();
        int num = 10000;
        for (int i = 0; i < num; i++) {
            src.add(Double.valueOf(Math.random()).intValue());
        }

        List<Integer> snapshot = new ArrayList<>(src);

        ExternalMergeSort.ExternalStore<Integer> ies = new ExternalStoreIntegerFileAdapter("D:/var/sort/externalMerge.tmp", src.size());
        for (int i=0; i<src.size(); i++) {
            ies.set(i, src.get(i));
        }

        ExternalMergeSort sort = new ExternalMergeSort();
        sort.sort(ies);

        Collections.sort(snapshot);

        for (int i=0; i<src.size(); i++) {
            Assert.assertEquals(src.get(i), snapshot.get(i));
        }

    }

}