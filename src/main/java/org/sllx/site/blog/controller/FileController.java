package org.sllx.site.blog.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sllx.core.util.FileUtils;
import org.sllx.site.core.base.BaseController;
import org.sllx.site.core.util.StaticResourceHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@org.springframework.stereotype.Controller
@RequestMapping("file")
public class FileController extends BaseController {

    protected static final Log logger = LogFactory.getLog(FileController.class);

    private static File fileDir = new File(StaticResourceHolder.getFileStorage());;

    static {
        try {
            FileUtils.forceMkdir(fileDir);
            logger.debug(String.format("directory [%s] created",fileDir));
        } catch (IOException e) {
            logger.error(String.format("failed to create directory [%s]", fileDir),e);
        }
    }

    /**
     * 上传文件
     * @param upload
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public void upload(@RequestParam MultipartFile upload, int CKEditorFuncNum, HttpServletResponse response) throws IOException {
        String fileName = UUID.randomUUID().toString();
        File img = new File(fileDir,fileName);
        upload.transferTo(img);
        logger.debug(String.format("image [%s] uploaded",fileName));
        PrintWriter out = response.getWriter();
        String script = String.format("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(%s,'%s/%s','');</script>",CKEditorFuncNum,selfURL(),fileName);
        out.println(script);
    }

    @RequestMapping(value = "{fileName:.*}",method = RequestMethod.GET)
    public void download(@PathVariable String fileName,HttpServletResponse response) throws IOException {
        File img = new File(fileDir,fileName);
        try{
            FileUtils.copyFileToOutputStream(img,response.getOutputStream());
            logger.debug(String.format("image [%s] downloaded",fileName));
        }catch (IOException e){
            logger.warn(String.format("failed to download image [%s]", fileName),e);
        }
    }
}
