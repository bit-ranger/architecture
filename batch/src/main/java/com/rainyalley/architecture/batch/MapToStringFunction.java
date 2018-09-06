package com.rainyalley.architecture.batch;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Function;

public class MapToStringFunction implements Function<Map<String,Object>, String> {

    private String delimiter = ",";

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String apply(Map<String,Object> item) {
        return StringUtils.join(item.values(), delimiter);
    }

}
