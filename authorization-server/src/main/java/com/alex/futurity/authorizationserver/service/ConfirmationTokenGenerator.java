package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationTokenGenerator {
    @Value("${confirmation.code.expired.time}")
    private int expiredTime;

    public ConfirmationToken generateToken(String email) {
        return generateToken(email, DateUtils.now());
    }

    public ConfirmationToken generateToken(String email, ZonedDateTime startExpiration) {
        String code = generateRandomNumberCode();

        return new ConfirmationToken(code, email, startExpiration.plusHours(expiredTime));
    }

    private String generateRandomNumberCode() {
        StringBuilder code = new StringBuilder();
        int codeLength = 6;

        for (int i = 0; i < codeLength; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 10);
            code.append(random);
        }

        return code.toString();
    }
}
