package com.rainyalley.architecture.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompressorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Compressor.class);

    @Test
    public void zip() {
        Compressor compressor = new Compressor();
        List<String> fileParts =  compressor.zip("/Download/6000/6000060004348157", 5000000);
        LOGGER.debug(String.valueOf(fileParts));
//        fileParts.forEach(p -> new File(p).delete());
    }
}