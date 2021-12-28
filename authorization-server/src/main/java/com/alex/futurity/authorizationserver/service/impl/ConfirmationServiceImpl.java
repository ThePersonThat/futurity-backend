package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDTO;
import com.alex.futurity.authorizationserver.dto.ConfirmEmailRequestDTO;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.service.ConfirmationService;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenService;
import com.alex.futurity.authorizationserver.service.EmailSenderService;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationTokenService tokenService;
    private final EmailSenderService emailService;
    private final HttpHelper httpHelper;
    @Value("${user-server}")
    private String userServerUrl;

    public ConfirmationServiceImpl(ConfirmationTokenService tokenService, EmailSenderService emailService, HttpHelper httpHelper) {
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.httpHelper = httpHelper;
    }

    @Override
    public void confirmEmail(ConfirmEmailRequestDTO dto) {
        if (httpHelper.doPost(userServerUrl + "/exist", dto, Boolean.class)) {
            throw new ClientSideException(String.format("%s already registered", dto.getEmail()), HttpStatus.CONFLICT);
        }

        String code = tokenService.generateConfirmationTokenForEmail(dto.getEmail());
        emailService.sendConfirmationMessage(dto.getEmail(), code);
    }

    @Override
    public void confirmCode(ConfirmCodeRequestDTO dto) {
        tokenService.confirmToken(dto);
    }

    @Override
    public boolean isEmailConfirmed(String email) {
        return tokenService.isEmailConfirmed(email);
    }
}
