package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.WrongTokenCodeException;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepo;
    @Value("${confirmation.code.expired.time}")
    private int expiredTime;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    @Transactional
    public String generateConfirmationTokenForEmail(String email) {

        deleteIfEmailExist(email);

        String code = generateRandomNumberCode();
        LocalDateTime expiredDate = LocalDateTime.now().plusHours(expiredTime);
        ConfirmationToken token = new ConfirmationToken(code, email, expiredDate);

        tokenRepo.save(token);

        return code;
    }

    @Override
    public void confirmToken(String email, String code) {
        ConfirmationToken token = tokenRepo.findByEmailAndCode(email, code)
                .orElseThrow(WrongTokenCodeException::new);

        tokenRepo.delete(token);
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

    private void deleteIfEmailExist(String email) {
        tokenRepo.deleteByEmail(email);
    }
}
