package com.alex.futurity.authorizationserver.utils;

import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileReaderTest {
    private FileReader reader;

    @BeforeEach
    private void intiReader() {
        reader = new FileReader();
    }

    @SneakyThrows
    private File createFile(String content) {
        File file = new File("/tmp/file");
        Files.writeString(file.toPath(), content);
        file.delete();

        return file;
    }

    @Test
    @DisplayName("Should read file")
    @SneakyThrows
    void testReadFileToString() {
        String content = "Hello world";
        String path = "any/path/";
        File file = createFile(content);

        try (MockedStatic<ResourceUtils> mockedResourceUtil = mockStatic(ResourceUtils.class);
             MockedStatic<Files> mockedFiles = mockStatic(Files.class)
        ) {
            mockedResourceUtil.when(() -> ResourceUtils.getFile(anyString())).thenReturn(file);
            mockedFiles.when(() -> Files.readAllBytes(any()))
                    .thenReturn(content.getBytes(StandardCharsets.UTF_8));

            String readContent = reader.readFileToString(path);

            assertThat(readContent).isEqualTo(content);
            mockedResourceUtil.verify(() -> ResourceUtils.getFile(eq("classpath:" + path)));
            mockedFiles.verify(() -> Files.readAllBytes(eq(file.toPath())));

        }
    }

    @Test
    @DisplayName("Should throw an IllegalStateException if a file cannot be read")
    void testReadFileWithError() {
        String exceptionMessage = "error read file";
        String path = "any/path/";
        String expectedMessage = String.format("Error loading \"%s\" file: " + exceptionMessage, path);

        try (MockedStatic<ResourceUtils> mockedResourceUtil = mockStatic(ResourceUtils.class);
                LogCaptor captor = LogCaptor.forClass(FileReader.class)) {

            mockedResourceUtil.when(() -> ResourceUtils.getFile(anyString()))
                    .thenThrow(new FileNotFoundException(exceptionMessage));

            assertThatThrownBy(() -> reader.readFileToString(path))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(expectedMessage);
            assertThat(captor.getLogs())
                    .hasSize(1)
                    .contains(expectedMessage);
        }
    }
}