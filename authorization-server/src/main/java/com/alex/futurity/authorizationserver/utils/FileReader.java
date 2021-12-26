package com.alex.futurity.authorizationserver.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Log4j2
public class FileReader {
    public String readFileToString(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            String message = String.format("Error loading \"%s\" file: " + e.getMessage(), path);
            log.error(message);

            throw new IllegalStateException(message);
        }
    }
}
