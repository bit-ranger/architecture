package com.rainyalley.architecture.batch;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import java.util.Map;

public class MapToStringFunction implements ItemProcessor<Map<String,Object>, String> {

    private String delimiter;


    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String process(Map<String,Object> item) throws Exception {
        return StringUtils.join(item.values(), delimiter);
    }
}
