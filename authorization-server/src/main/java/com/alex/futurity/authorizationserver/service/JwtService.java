package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.dto.JwtTokenDTO;

public interface JwtService {
    JwtRefreshResponseDTO refreshAccessToken(JwtTokenDTO request);
    JwtRefreshResponseDTO generateAccessAndRefreshTokenPair(Long id);
}
