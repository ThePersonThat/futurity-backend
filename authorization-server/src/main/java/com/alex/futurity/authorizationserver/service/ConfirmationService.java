package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.client.UserServiceClient;
import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDto;
import com.alex.futurity.authorizationserver.dto.ConfirmEmailRequestDto;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationService {
    private final ConfirmationTokenService tokenService;
    private final EmailSenderService emailService;
    private final UserServiceClient userServiceClient;

    public void confirmEmail(ConfirmEmailRequestDto dto) {
        if (userServiceClient.userExists(dto)) {
            throw new ClientSideException(String.format("%s already registered", dto.getEmail()), HttpStatus.CONFLICT);
        }

        String code = tokenService.generateConfirmationTokenForEmail(dto.getEmail());
        emailService.sendConfirmationMessage(dto.getEmail(), code);
    }

    public void confirmCode(ConfirmCodeRequestDto dto) {
        tokenService.confirmToken(dto);
    }

    public boolean isEmailConfirmed(String email) {
        return tokenService.isEmailConfirmed(email);
    }
}
