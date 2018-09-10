package com.rainyalley.architecture.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 压缩器
 */
public class Compressor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Compressor.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * @param src 源文件路径
     * @param dst 压缩文件路径, 为空表示与源文件同名同级
     * @param password 压缩密码, 为空表示无密码
     * @param charset 字符集
     * @param perFileSize 每个分卷的大小
     */
    @SuppressWarnings("unchecked")
    public  List<String> zip(String src, String dst, String password, long perFileSize, Charset charset) {
        ArrayList zipList = null;

        // 压缩参数设置
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password.toCharArray());
        }

        // 压缩文件目标路径预处理
        File srcFile = new File(src);
        if (StringUtils.isBlank(dst)) {
            dst = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
        }
        createDirIfNecessary(dst);

        try {
            ZipFile zipFile = new ZipFile(dst);
            zipFile.createZipFileFromFolder(srcFile, parameters, true, perFileSize);
            zipList = zipFile.getSplitZipFiles();
        } catch (ZipException e) {
            LOGGER.error(String.format("zip error, args %s", Arrays.asList(src, dst, password, perFileSize, charset)), e);
        }

        return (List<String>) zipList;
    }


    public  List<String> zip(String src, long perFileSize){
        return zip(src, "", "", perFileSize, DEFAULT_CHARSET);
    }

    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param filePath 指定的存放路径,有可能该路径并没有被创建
     */
    private boolean createDirIfNecessary(String filePath) {
        File dir = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
        if (!dir.exists()) {
            return dir.mkdirs();
        } else {
            return false;
        }
    }
}
