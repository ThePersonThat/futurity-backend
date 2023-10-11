package com.alex.futurity.authorizationserver.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@UtilityClass
public class FileReader {
    public static String readFileToString(String path) {
        try {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + path);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new IllegalStateException(
                    String.format("Error loading \"%s\" file: " + e.getMessage(), path)
            );
        }
    }
}
