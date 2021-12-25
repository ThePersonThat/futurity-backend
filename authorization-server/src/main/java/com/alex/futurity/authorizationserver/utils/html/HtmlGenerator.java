package com.alex.futurity.authorizationserver.utils.html;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

@Log4j2
public abstract class HtmlGenerator {
    protected String html;

    protected void readHtmlFile(String filename) {
        try {
            File htmlFile = ResourceUtils.getFile("classpath:" + filename);
            html = new String(Files.readAllBytes(htmlFile.toPath()));
        } catch (Exception e) {
            String message = "Error loading html file: " + e.getMessage();
            log.error(message);

            throw new IllegalStateException(message);
        }
    }

    public abstract String generateHtml(String... values);
}
