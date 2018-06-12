package com.rainyalley.architecture.batch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class NopItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> list) throws Exception {

    }
}
