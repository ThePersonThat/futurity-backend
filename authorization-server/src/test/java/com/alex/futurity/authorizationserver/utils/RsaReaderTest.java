package com.alex.futurity.authorizationserver.utils;

import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RsaReaderTest {
    private RsaReader reader;

    @SneakyThrows
    private static File getRSAFile(String content) {
        File file = new File("/tmp/testRsa.pem");
        Files.writeString(file.toPath(), content);

        return file;
    }

    @BeforeEach
    private void init() {
        reader = new RsaReader();
    }

    @Test
    @DisplayName("Should read a valid rsa key")
    void testReadValidKey() {
        try (MockedStatic<ResourceUtils> mockedResourceUtil = mockStatic(ResourceUtils.class)) {
            String validRSAKey = "-----BEGIN PRIVATE KEY-----\n" +
                    "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkBpE9DhsZiLE2uHQQTi\n" +
                    "QyL/5PuBw1itUJqQJoR8mHrwDSixIbJ/HVTVK7IIoQ3njK9l10Lbz2e6gwqnMvET\n" +
                    "0CrZAgMBAAECQCqpugi449er5d+/nW0El0RQw3E3UFzmOitHCEnSWy/1j8/roNNL\n" +
                    "X5ET2dcaM/gByX/DYNEq13uD+KOqWTB8JPUCIQDJTGtABP9tof4cxQal0GQwrFQq\n" +
                    "dcC4YvOF9EjIuKhUjwIhAIWhp//Jw/qSZpjEOSiokcLQv10zj6BW+qAS1Y+Ww04X\n" +
                    "AiEAxz/zMQ1BX91m0Eddyyc45MzPCrA/sMORVNh2augNBpUCIHF6TrTLPO57Bj7y\n" +
                    "WuupAmaiZ+8Yiv4I/rmSZOKTj3SvAiEAv98oO/IbLp+j4Q2aBkiNjqqgdQ9KLwM1\n" +
                    "/JSs+qXvHbE=\n" +
                    "-----END PRIVATE KEY-----\n";

            File keyFile = getRSAFile(validRSAKey);
            mockedResourceUtil.when(() -> ResourceUtils.getFile(anyString())).thenReturn(keyFile);

            ReflectionTestUtils.invokeMethod(reader, "readPrivateKeyFile");

            assertThat(reader.getPrivateKey()).isNotNull();
        }
    }

    private static Stream<File> getInvalidKeyFiles() {
        String withInvalidHeader = "-----BEGIN PTE KEY-----\n" +
                "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkBpE9DhsZiLE2uHQQTi\n" +
                "QyL/5PuBw1itUJqQJoR8mHrwDSixIbJ/HVTVK7IIoQ3njK9l10Lbz2e6gwqnMvET\n" +
                "0CrZAgMBAAECQCqpugi449er5d+/nW0El0RQw3E3UFzmOitHCEnSWy/1j8/roNNL\n" +
                "X5ET2dcaM/gByX/DYNEq13uD+KOqWTB8JPUCIQDJTGtABP9tof4cxQal0GQwrFQq\n" +
                "dcC4YvOF9EjIuKhUjwIhAIWhp//Jw/qSZpjEOSiokcLQv10zj6BW+qAS1Y+Ww04X\n" +
                "AiEAxz/zMQ1BX91m0Eddyyc45MzPCrA/sMORVNh2augNBpUCIHF6TrTLPO57Bj7y\n" +
                "WuupAmaiZ+8Yiv4I/rmSZOKTj3SvAiEAv98oO/IbLp+j4Q2aBkiNjqqgdQ9KLwM1\n" +
                "/JSs+qXvHbE=\n" +
                "-----END PRIVATE KEY-----\n";

        String withNotPCKS8Format = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIBOgIBAAJAaRPQ4bGYixNrh0EE4kMi/+T7gcNYrVCakCaEfJh68A0osSGyfx1U\n" +
                "1SuyCKEN54yvZddC289nuoMKpzLxE9Aq2QIDAQABAkAqqboIuOPXq+Xfv51tBJdE\n" +
                "UMNxN1Bc5jorRwhJ0lsv9Y/P66DTS1+RE9nXGjP4Acl/w2DRKtd7g/ijqlkwfCT1\n" +
                "AiEAyUxrQAT/baH+HMUGpdBkMKxUKnXAuGLzhfRIyLioVI8CIQCFoaf/ycP6kmaY\n" +
                "xDkoqJHC0L9dM4+gVvqgEtWPlsNOFwIhAMc/8zENQV/dZtBHXcsnOOTMzwqwP7DD\n" +
                "kVTYdmroDQaVAiBxek60yzzuewY+8lrrqQJmomfvGIr+CP65kmTik490rwIhAL/f\n" +
                "KDvyGy6fo+ENmgZIjY6qoHUPSi8DNfyUrPql7x2x\n" +
                "-----END PRIVATE KEY-----";

        return Stream.of(
                getRSAFile(withInvalidHeader),
                getRSAFile(withNotPCKS8Format)
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw an IllegalStateException if a key is invalid")
    @MethodSource("getInvalidKeyFiles")
    void testInvalidKey(File invalidKeyFile) {
        try (MockedStatic<ResourceUtils> mockedResourceUtil = mockStatic(ResourceUtils.class)) {
            LogCaptor log = LogCaptor.forClass(RsaReader.class);
            mockedResourceUtil.when(() -> ResourceUtils.getFile(anyString())).thenReturn(invalidKeyFile);

            assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(reader, "readPrivateKeyFile"))
                    .isInstanceOf(IllegalStateException.class);

            assertThat(log.getLogs()).hasSize(1);
        }
    }
}