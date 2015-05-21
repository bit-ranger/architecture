package top.rainynight.site.blog.controller;

import org.apache.commons.io.IOUtils;
import top.rainynight.site.blog.entity.Archive;
import top.rainynight.site.blog.service.ArchiveService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@org.springframework.stereotype.Controller
@RequestMapping("file")
public class FileController{

    @javax.annotation.Resource(name = "archiveService")
    private ArchiveService archiveService;

    @RequestMapping(method = RequestMethod.POST)
    public void upload(@RequestParam MultipartFile upload, int CKEditorFuncNum, Archive archive, HttpServletResponse response) throws IOException {
        String fileName = UUID.randomUUID().toString();

        archive.setName(fileName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(upload.getInputStream(), bos);
        archive.setBody(bos.toByteArray());

        archiveService.insert(archive);
        PrintWriter out = response.getWriter();
        String script = String.format("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(%s,'%s/%s','');</script>",CKEditorFuncNum,"file",fileName);
        out.println(script);
    }

    @RequestMapping(value = "{fileName:.*}",method = RequestMethod.GET)
    public void download(@PathVariable String fileName, Archive archive, HttpServletResponse response) throws IOException {
        archive.setName(fileName);
        archive = archiveService.get(archive);
        IOUtils.write(archive.getBody(), response.getOutputStream());
    }
}
