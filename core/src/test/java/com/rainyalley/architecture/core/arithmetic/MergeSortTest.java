package com.rainyalley.architecture.core.arithmetic;

import org.junit.Test;

import java.util.Arrays;

public class MergeSortTest {

    @Test
    public void sort() throws Exception {
        Integer array[] = new Integer[]{33,67,98,9,23,56,67};
        MergeSort sort = new MergeSort();
        sort.sort(array);
        Arrays.asList(array).forEach(p -> System.out.println(p));
    }

}