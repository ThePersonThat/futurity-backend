package com.alex.futurity.authorizationserver.service;

public interface ConfirmationTokenService {
    String generateConfirmationTokenForEmail(String email);
    void confirmToken(String email, String code);
}
