package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.*;
import com.alex.futurity.authorizationserver.exception.ClientSideException;
import com.alex.futurity.authorizationserver.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RegistrationUserService registrationService;
    private final LoginUserService loginService;
    private final ConfirmationService confirmationService;

    @Override
    public JwtTokenResponseDTO login(LoginRequestDTO dto) {
        return loginService.loginUser(dto);
    }

    @Override
    public void singUp(SingUpRequestDTO request, MultipartFile avatar) {
        if (confirmationService.isEmailConfirmed(request.getEmail())) {
            registrationService.registerUser(request, avatar);
        } else {
            throw new ClientSideException(String.format("%s is not confirmed", request.getEmail()), HttpStatus.CONFLICT);
        }
    }

    @Override
    public void confirmCode(ConfirmCodeRequestDTO confirmDto) {
        confirmationService.confirmCode(confirmDto);
    }

    @Override
    public void confirmEmail(ConfirmEmailRequestDTO confirmDto) {
        confirmationService.confirmEmail(confirmDto);
    }
}
