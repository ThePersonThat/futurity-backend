package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.client.UserServiceClient;
import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDto;
import com.alex.futurity.authorizationserver.dto.JwtTokenDto;
import com.alex.futurity.authorizationserver.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService {
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;

    public JwtRefreshResponseDto loginUser(LoginRequestDto dto) {
        LoginDomain response = userServiceClient.login(dto);

        return jwtService.generateAccessAndRefreshTokenPair(response.getId());
    }

    public JwtTokenDto refreshToken(JwtTokenDto request) {
        return jwtService.refreshAccessToken(request);
    }
}
