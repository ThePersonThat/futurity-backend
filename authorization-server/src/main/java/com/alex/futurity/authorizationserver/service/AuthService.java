package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.JwtTokenResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    JwtTokenResponseDTO login(LoginRequestDTO request);
    void singUp(SingUpRequestDTO request, MultipartFile avatarDto);
}
