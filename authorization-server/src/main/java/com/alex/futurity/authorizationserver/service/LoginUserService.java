package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.JwtTokenResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;

public interface LoginUserService {
    JwtTokenResponseDTO loginUser(LoginRequestDTO dto);
}
