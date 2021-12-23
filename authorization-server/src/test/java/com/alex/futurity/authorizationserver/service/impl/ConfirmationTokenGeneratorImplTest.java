package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

class ConfirmationTokenGeneratorImplTest {
    private ConfirmationTokenGenerator tokenGenerator;
    private final int expiredTime = 24;

    @BeforeEach
    private void init() {
        tokenGenerator = new ConfirmationTokenGeneratorImpl(expiredTime);
    }

    @Test
    @DisplayName("Should generate confirmation token")
    void testGenerateConfirmationToken() {
        String email = "alex@jpeg.com";
        LocalDateTime now = LocalDateTime.now();
        ConfirmationToken token = tokenGenerator.generateToken(email, now);

        assertThat(token.getEmail()).isEqualTo(email);
        assertThat(token.getCode())
                .isNotEmpty()
                .hasSize(6);

        assertThat(token.getExpiredAt().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(now.plusHours(expiredTime).truncatedTo(ChronoUnit.SECONDS));
    }
}