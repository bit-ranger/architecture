package com.rainyalley.architecture.core.arithmetic;

import org.junit.Test;

public class ExternalMergeSortTest {
    @Test
    public void sort() throws Exception {
        Integer array[] = new Integer[]{33,67,98,9,23,56,67};
        ExternalMergeSort.ExternalStore<Integer> ies = new ExternalStoreIntegerFileAdapter("D:\\var\\sort", array.length);
        for (int i=0; i<array.length; i++) {
            ies.set(i, array[i]);
        }

        ExternalMergeSort sort = new ExternalMergeSort();
        sort.sort(ies);

        for (int i=0; i<array.length; i++) {
            System.out.println(ies.get(i));
        }
    }

}