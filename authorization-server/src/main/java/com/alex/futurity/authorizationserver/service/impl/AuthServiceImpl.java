package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDTO;
import com.alex.futurity.authorizationserver.dto.JwtTokenResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import com.alex.futurity.authorizationserver.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RegistrationUserService registrationService;
    private final LoginUserService loginService;
    private final ConfirmationTokenService tokenService;

    @Override
    public JwtTokenResponseDTO login(LoginRequestDTO dto) {
        return loginService.loginUser(dto);
    }

    @Override
    public void singUp(SingUpRequestDTO request, MultipartFile avatar) {
        registrationService.registerUser(request, avatar);
    }

    @Override
    public void confirmCode(ConfirmCodeRequestDTO confirmDto) {
        tokenService.confirmToken(confirmDto);
    }
}
