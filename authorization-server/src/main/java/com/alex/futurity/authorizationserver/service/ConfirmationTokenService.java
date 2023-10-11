package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDto;
import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.repo.ConfirmationTokenRepository;
import com.alex.futurity.authorizationserver.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepo;
    private final ConfirmationTokenGenerator tokenGenerator;

    @Transactional
    public String generateConfirmationTokenForEmail(String email) {
        deleteIfEmailExist(email);
        ConfirmationToken token = tokenGenerator.generateToken(email);

        tokenRepo.save(token);
        return token.getCode();
    }

    @Transactional
    public void confirmToken(ConfirmCodeRequestDto confirmDto) {
        tokenRepo.findByEmailAndCodeAndConfirmedFalse(confirmDto.getEmail(), confirmDto.getCode())
                .filter(token -> DateUtils.isNotInPast(token.getExpiredAt()))
                .map(token -> confirmToken(confirmDto, token))
                .orElseThrow(() -> new ClientSideException("Code is expired. Try again", HttpStatus.GONE));
    }

    private ConfirmationToken confirmToken(ConfirmCodeRequestDto confirmDto, ConfirmationToken token) {
        log.info("Code {} for {} have been confirmed", confirmDto.getCode(), confirmDto.getEmail());

        return token
                .setConfirmedAt(DateUtils.now())
                .setConfirmed(true);
    }

    public boolean isEmailConfirmed(String email) {
        return tokenRepo.findByEmailAndConfirmedTrue(email).isPresent();
    }

    private void deleteIfEmailExist(String email) {
        tokenRepo.deleteByEmail(email);
    }
}
