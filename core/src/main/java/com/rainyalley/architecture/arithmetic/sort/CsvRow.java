package com.rainyalley.architecture.arithmetic.sort;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CsvRow implements Comparable<CsvRow> {

    private String line;

    private List<String> dataList = new ArrayList<>(8);

    private static final String dataSeparator = ",";

    public CsvRow(String line) {
        this.line = line;
        String[] datas = StringUtils.split(line, dataSeparator);
        for (String data : datas) {
            dataList.add(data.trim());
        }
    }

    @Override
    public String toString() {
        return line;
    }

    @Override
    public int compareTo(@NotNull CsvRow o) {
        return Long.valueOf(dataList.get(0)).compareTo(Long.valueOf(o.dataList.get(0)));
    }


}
