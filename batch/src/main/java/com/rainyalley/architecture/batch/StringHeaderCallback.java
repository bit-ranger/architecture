package com.rainyalley.architecture.batch;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.IOException;
import java.io.Writer;

public class StringHeaderCallback implements FlatFileHeaderCallback {

    private String header;

    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write(header);
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
