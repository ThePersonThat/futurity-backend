package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDTO;
import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenGenerator;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Log4j2
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
    public void confirmToken(ConfirmCodeRequestDTO confirmDto) {
        ConfirmationToken token = tokenRepo.findByEmailAndCodeAndConfirmedTrue(confirmDto.getEmail(), confirmDto.getCode())
                .orElseThrow(() -> new ClientSideException(String.format("Wrong code for %s. Check the code again",
                        confirmDto.getEmail()), HttpStatus.NOT_FOUND));

        token.setConfirmedAt(LocalDateTime.now());
        token.setConfirmed(true);
        tokenRepo.save(token);

        log.info("Code {} for {} have been confirmed", confirmDto.getCode(), confirmDto.getEmail());
    }

    @Override
    public boolean isEmailConfirmed(String email) {
        return tokenRepo.findByEmailAndConfirmedTrue(email).isPresent();
    }

    private void deleteIfEmailExist(String email) {
        tokenRepo.deleteByEmail(email);
    }
}
