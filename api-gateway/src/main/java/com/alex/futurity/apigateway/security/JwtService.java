package com.alex.futurity.apigateway.security;

public interface JwtService {
    void verifyToken(String token);
}
