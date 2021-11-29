package com.alex.futurity.authorizationserver.service;

public interface JwtService {
    String generateAccessToken(Long id);
}
