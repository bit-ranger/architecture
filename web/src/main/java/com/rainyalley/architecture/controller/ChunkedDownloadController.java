package com.rainyalley.architecture.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/download")
public class ChunkedDownloadController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/csv", method = RequestMethod.GET)
    public void csv(HttpServletResponse response)throws IOException {
        Charset charset = Charset.forName("UTF-8");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), charset));

        try {
            Path path = Paths.get(this.getClass().getResource("/architecture_user.csv").toURI());

            BufferedReader reader = Files.newBufferedReader(path, charset);

            response.setContentType(new MediaType("application", "csv").toString());
            response.setHeader("Content-Disposition", "attachment; filename=architecture_user.csv");
            response.setHeader("Transfer-Encoding", "chunked");


            String line = null;
            while ((line = reader.readLine()) != null){
                int size = line.getBytes(charset).length;
                writer.write(Integer.toHexString(size));
                writer.newLine();
                writer.write(line);
                writer.newLine();
                writer.newLine();
                writer.flush();
            }

            writer.write(Integer.toHexString(0));
            writer.newLine();
            writer.newLine();
            writer.flush();
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
