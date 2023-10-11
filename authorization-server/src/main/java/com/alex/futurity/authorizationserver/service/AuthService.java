package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.*;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class AuthService {
    private final RegistrationUserService registrationService;
    private final LoginUserService loginService;
    private final ConfirmationService confirmationService;

    public JwtRefreshResponseDto login(LoginRequestDto dto) {
        return loginService.loginUser(dto);
    }

    public void singUp(SingUpRequestDto request, MultipartFile avatar) {
//        if (confirmationService.isEmailConfirmed(request.getEmail())) {
            registrationService.registerUser(request, avatar);
//        } else {
//            throw new ClientSideException(String.format("%s is not confirmed", request.getEmail()), HttpStatus.FORBIDDEN);
//        }
    }

    public void confirmCode(ConfirmCodeRequestDto confirmDto) {
        confirmationService.confirmCode(confirmDto);
    }

    public void confirmEmail(ConfirmEmailRequestDto confirmDto) {
        confirmationService.confirmEmail(confirmDto);
    }

    public JwtTokenDto refreshToken(JwtTokenDto request) {
        return loginService.refreshToken(request);
    }
}
