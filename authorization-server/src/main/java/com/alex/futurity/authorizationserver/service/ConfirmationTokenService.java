package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDTO;

public interface ConfirmationTokenService {
    String generateConfirmationTokenForEmail(String email);
    void confirmToken(ConfirmCodeRequestDTO confirmDto);
    boolean isEmailConfirmed(String email);
}
