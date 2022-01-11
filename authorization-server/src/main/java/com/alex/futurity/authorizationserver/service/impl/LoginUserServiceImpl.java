package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.dto.JwtTokenDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.service.JwtService;
import com.alex.futurity.authorizationserver.service.LoginUserService;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LoginUserServiceImpl implements LoginUserService {
    private final HttpHelper httpHelper;
    private final JwtService jwtService;

    @Value("${user-server}")
    private String userServerUrl;
    private String url;

    public LoginUserServiceImpl(HttpHelper httpHelper, JwtService jwtService) {
        this.httpHelper = httpHelper;
        this.jwtService = jwtService;
    }

    @PostConstruct
    private void setupUrl() {
        this.url = userServerUrl + "/login";
    }

    @Override
    public JwtRefreshResponseDTO loginUser(LoginRequestDTO dto) {
        LoginDomain login = httpHelper.doPost(url, dto, LoginDomain.class);

        return jwtService.generateAccessAndRefreshTokenPair(login.getId());
    }

    @Override
    public JwtTokenDTO refreshToken(JwtTokenDTO request) {
        return jwtService.refreshAccessToken(request);
    }
}
