package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.WrongTokenCodeException;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenGenerator;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepo;
    private final ConfirmationTokenGenerator tokenGenerator;

    @Override
    @Transactional
    public String generateConfirmationTokenForEmail(String email) {
        deleteIfEmailExist(email);
        ConfirmationToken token = tokenGenerator.generateToken(email);

        tokenRepo.save(token);
        return token.getCode();
    }

    @Override
    @Transactional
    public void confirmToken(String email, String code) {
        ConfirmationToken token = tokenRepo.findByEmailAndCode(email, code)
                .orElseThrow(WrongTokenCodeException::new);

        tokenRepo.delete(token);
    }

    private void deleteIfEmailExist(String email) {
        tokenRepo.deleteByEmail(email);
    }
}
