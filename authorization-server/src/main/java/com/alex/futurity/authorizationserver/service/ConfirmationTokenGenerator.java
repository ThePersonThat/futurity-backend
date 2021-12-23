package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;

import java.time.LocalDateTime;

public interface ConfirmationTokenGenerator {
    ConfirmationToken generateToken(String email);
    ConfirmationToken generateToken(String email, LocalDateTime startExpiration);
}
