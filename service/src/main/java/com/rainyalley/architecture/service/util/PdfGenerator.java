package com.rainyalley.architecture.service.util;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jcip.annotations.Immutable;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.util.Assert;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

@Immutable
public class PdfGenerator implements Closeable{


    private static final String defaultEncoding = "UTF-8";

    private static final String defaultFontDir =  PdfGenerator.class.getResource("/").getPath() + "font";

    private String encoding = defaultEncoding;

    private Configuration config;

    protected GenericObjectPool<ITextRenderer> rendererPool;


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

        List<String> fontPathList = new ArrayList<>(fonts.length);

        for (File file : fonts) {
            if(file.isDirectory()){
                continue;
            }

            fontPathList.add(file.getPath());
        }

        rendererPool = new GenericObjectPool<ITextRenderer>(new RendererFactory(fontPathList));
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

        ITextRenderer renderer = null;
        try {
            renderer = rendererPool.borrowObject();
        } catch (Exception e) {
            throw new MissingResourceException("Could not get a resource from the pool", ITextRenderer.class.getName(), "");
        }
        try {
            renderer.setDocumentFromString(content);
            renderer.layout();
            renderer.createPDF(out);
            renderer.finishPDF();
        } finally {
            if (renderer != null) {
                rendererPool.returnObject(renderer);
            }
        }
    }

    @Override
    public void close() throws IOException {
        rendererPool.close();
    }

    private static class RendererFactory implements PooledObjectFactory<ITextRenderer>{

        private List<String> fontPathList;

        private RendererFactory(List<String> fontPathList) {
            this.fontPathList = fontPathList;
        }

        @Override
        public PooledObject<ITextRenderer> makeObject() throws Exception {
            ITextRenderer render = new ITextRenderer();

            ITextFontResolver resolver = render.getFontResolver();

            for (String font : fontPathList) {
                resolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
            return new DefaultPooledObject<>(render);
        }

        @Override
        public void destroyObject(PooledObject<ITextRenderer> p) throws Exception {
        }

        @Override
        public boolean validateObject(PooledObject<ITextRenderer> p) {
            return true;
        }

        @Override
        public void activateObject(PooledObject<ITextRenderer> p) throws Exception {
        }

        @Override
        public void passivateObject(PooledObject<ITextRenderer> p) throws Exception {
        }
    }
}
