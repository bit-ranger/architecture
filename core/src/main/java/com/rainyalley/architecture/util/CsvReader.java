package com.rainyalley.architecture.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvReader extends Reader {


    private BufferedReader reader;

    private String separator = ",";

    /**
     * 一条字符串转换为字段数组
     */
    private Function<String, List<String>> mapper = line -> Arrays.asList(StringUtils.split(line, separator));

    /**
     *
     * @param reader 按行读取器
     */
    public CsvReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    /**
     *
     * @param reader 按行读取器
     * @param separator 分隔符
     */
    public CsvReader(Reader reader, String separator) {
        this.reader = new BufferedReader(reader);
        this.separator = separator;
    }

    @Override
    public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }


    public List<String> readRow() throws IOException{
        String line = reader.readLine();
        if(line == null){
            return null;
        }
        return mapper.apply(line);
    }

    public Stream<List<String>> rows(){
        return reader.lines().map(mapper);
    }

}
