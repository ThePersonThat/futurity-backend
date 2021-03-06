package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    JwtRefreshResponseDTO login(LoginRequestDTO request);
    void singUp(SingUpRequestDTO request, MultipartFile avatarDto);
    void confirmCode(ConfirmCodeRequestDTO confirmDto);
    void confirmEmail(ConfirmEmailRequestDTO confirmDto);
    JwtTokenDTO refreshToken(JwtTokenDTO request);
}
