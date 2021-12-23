package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenGenerator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationTokenGeneratorImpl implements ConfirmationTokenGenerator {
    @Value("${confirmation.code.expired.time}")
    private int expiredTime;

    @Override
    public ConfirmationToken generateToken(String email) {
        return generateToken(email, LocalDateTime.now());
    }

    @Override
    public ConfirmationToken generateToken(String email, LocalDateTime startExpiration) {
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
