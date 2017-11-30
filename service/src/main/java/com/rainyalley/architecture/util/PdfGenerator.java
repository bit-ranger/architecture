package com.rainyalley.architecture.util;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jcip.annotations.Immutable;
import org.springframework.util.Assert;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Immutable
public class PdfGenerator {


    private static final String defaultEncoding = "UTF-8";

    private static final String defaultFontDir =  PdfGenerator.class.getResource("/").getPath() + "/font";

    private String encoding = defaultEncoding;

    private Configuration config;

    private List<String> fontPathList;


    public PdfGenerator(String ftlDir) {
        this(ftlDir, defaultEncoding, defaultFontDir);
    }

    public PdfGenerator(String ftlDir, String encoding, String fontDir) {
        this.encoding = encoding;

        config = new Configuration();
        if(!ftlDir.startsWith("/")){
            ftlDir = "/" + ftlDir;
        }
        config.setClassForTemplateLoading(this.getClass(), ftlDir);
        config.setEncoding(Locale.CHINA, encoding);

        File fontDirFile = new File(fontDir);

        Assert.isTrue(fontDirFile.isDirectory(), String.format("not a dir [%s]", fontDir));

        File[] fonts = fontDirFile.listFiles();

        Assert.notNull(fonts, String.format("no files in dir [%s]", fontDir));

        fontPathList = new ArrayList<>(fonts.length);

        for (File file : fonts) {
            if(file.isDirectory()){
                continue;
            }

            fontPathList.add(file.getPath());
        }
    }

    // 使用freemarker得到html内容
    public String resolve(String ftlName, Object data) throws IOException, TemplateException {
        Template tpl = config.getTemplate(ftlName);
        tpl.setEncoding(encoding);
        StringWriter writer = new StringWriter();
        tpl.process(data, writer);
        writer.flush();
        return  writer.toString();
    }

    public void write(String content, OutputStream out) throws DocumentException, IOException {

        ITextRenderer render = new ITextRenderer();

        ITextFontResolver resolver = render.getFontResolver();

        for (String font : fontPathList) {
            resolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }

        render.setDocumentFromString(content);
        render.layout();
        render.createPDF(out);
        render.finishPDF();
    }
}
