package org.sllx.site.blog.controller;

import org.sllx.site.core.common.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by sllx on 14-11-27.
 */
@org.springframework.stereotype.Controller
@RequestMapping("/file")
public class FileController extends Controller{

    private final static String fileDir = "E:/tmp";

    /**
     * 上传文件
     * @param upload
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public void upload(@RequestParam MultipartFile upload, int CKEditorFuncNum, HttpServletResponse response) throws IOException {
        String fileName = upload.getOriginalFilename();
        File img = new File(fileDir + "/" + fileName);
        upload.transferTo(img);

        PrintWriter out = response.getWriter();
        String scritpt = String.format("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(%s,'%s/%s','');</script>",CKEditorFuncNum,getSelfHref(),fileName);
        out.println(scritpt);
    }

    @RequestMapping(value = "/{fileName}",method = RequestMethod.GET)
    public void download(@PathVariable String fileName,HttpServletResponse response) throws IOException {
        File img = new File(fileDir + "/" + fileName + ".jpg");
        response.getOutputStream().write(toinputStream(img));
    }

    public static byte[] toinputStream(File file) throws IOException {
        byte[] a = new byte[1048576];
        FileInputStream fis;
        fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(a);
        return a;
    }
}
