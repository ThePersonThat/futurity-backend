package com.alex.futurity.apigateway.util;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RsaReaderTest {
    @Mock
    private FileReader fileReader;
    @InjectMocks
    private RsaReader rsaReader;

    @Test
    @DisplayName("Should read a valid rsa key")
    void testReadValidKey() {
        String validRSAKey = "-----BEGIN PUBLIC KEY-----\n" +
               "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyJDN6pQk1e+rfuy+kj3W\n" +
                "SwjUFT8H7oQU5NRNYufQm6n9bYYu/lClY2+RrfxWyTpuGyV73qj5wZy3op74AgPY\n" +
                "ixsXp40SwlCEnt7+U80r7rBUT/4MUbx8yc6tcj6Sdg50GxEHw7WvqFZBh6P/Xl+6\n" +
                "x7EyubPTDsQGKeH6QkaGX24kFyC/lHoVGETweXKqAc3YYNqCFPe+nhMOi5clu+6+\n" +
                "QYKx5365FEqz11TSD22voYgkwsMguuD5du749wfj4u3zISxkyUYr8OHUQKJU92dJ\n" +
                "KlVp468H4etA8zvBzLJhYdukPrQ2GbMvR0EtZkzYkyABB21n4aaUmdu7+bGgYdaG\n" +
                "+QIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";

        when(fileReader.readFileToString(any())).thenReturn(validRSAKey);

        ReflectionTestUtils.invokeMethod(rsaReader, "readPublicKey");

        assertThat(rsaReader.getPublicKey()).isNotNull();
    }

    @Test
    @DisplayName("Should throw an IllegalStateException if a key is invalid")
    void testInvalidKey() {
        String invalidKey = "-----BEGIN PTE KEY-----\n" +
                "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkBpE9DhsZiLE2uHQQTi\n" +
                "QyL/5PuBw1itUJqQJoR8mHrwDSixIbJ/HVTVK7IIoQ3njK9l10Lbz2e6gwqnMvET\n" +
                "0CrZAgMBAAECQCqpugi449er5d+/nW0El0RQw3E3UFzmOitHCEnSWy/1j8/roNNL\n" +
                "X5ET2dcaM/gByX/DYNEq13uD+KOqWTB8JPUCIQDJTGtABP9tof4cxQal0GQwrFQq\n" +
                "dcC4YvOF9EjIuKhUjwIhAIWhp//Jw/qSZpjEOSiokcLQv10zj6BW+qAS1Y+Ww04X\n" +
                "AiEAxz/zMQ1BX91m0Eddyyc45MzPCrA/sMORVNh2augNBpUCIHF6TrTLPO57Bj7y\n" +
                "WuupAmaiZ+8Yiv4I/rmSZOKTj3SvAiEAv98oO/IbLp+j4Q2aBkiNjqqgdQ9KLwM1\n" +
                "/JSs+qXvHbE=\n" +
                "-----END PUBLIC KEY-----\n";

        LogCaptor log = LogCaptor.forClass(RsaReader.class);
        when(fileReader.readFileToString(any())).thenReturn(invalidKey);

        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(rsaReader, "readPublicKey"))
                .isInstanceOf(IllegalStateException.class);

        assertThat(log.getLogs()).hasSize(1);
    }
}