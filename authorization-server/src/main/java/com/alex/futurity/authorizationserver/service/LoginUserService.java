package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.dto.JwtTokenDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;

public interface LoginUserService {
    JwtRefreshResponseDTO loginUser(LoginRequestDTO dto);
    JwtTokenDTO refreshToken(JwtTokenDTO request);
}
