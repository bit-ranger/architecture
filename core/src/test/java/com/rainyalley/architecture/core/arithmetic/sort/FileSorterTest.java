package com.rainyalley.architecture.core.arithmetic.sort;

import org.junit.Test;

import java.io.File;

public class FileSorterTest {

    @Test
    public void sort() throws Exception {
        FileSorter sorter = new FileSorter((p,n) -> p.compareTo(n),
                8, 10000, 1024*1024*2, new File("/var/tmp/fileSorter"), false);

        sorter.sort(new File("/var/architecture_user.new.csv"), new File("/var/architecture_user.new.sorted.csv"));
    }

}