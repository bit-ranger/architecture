package com.rainyalley.architecture.core.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
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

    /**
     *
     * @param in 输入流
     * @param charset 字符集
     */
    public CsvReader(InputStream in, Charset charset) {
        this(new InputStreamReader(in, charset));
    }

    /**
     *
     * @param in 输入流
     * @param charset 字符集
     * @param separator 分隔符
     */
    public CsvReader(InputStream in, Charset charset, String separator) {
        this(new InputStreamReader(in, charset), separator);
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
