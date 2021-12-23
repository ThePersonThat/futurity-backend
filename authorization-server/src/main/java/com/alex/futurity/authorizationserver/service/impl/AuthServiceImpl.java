package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.JwtTokenResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import com.alex.futurity.authorizationserver.service.AuthService;
import com.alex.futurity.authorizationserver.service.JwtService;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final HttpHelper httpHelper;
    private final JwtService jwtService;

    @Value("${user-server}")
    private String userServerUrl;

    public AuthServiceImpl(HttpHelper httpHelper, JwtService jwtService) {
        this.httpHelper = httpHelper;
        this.jwtService = jwtService;
    }

    @Override
    public JwtTokenResponseDTO login(LoginRequestDTO dto) {
        LoginDomain login = httpHelper.doPost(userServerUrl + "/login", dto, LoginDomain.class);
        String token = jwtService.generateAccessToken(login.getId());

        return new JwtTokenResponseDTO(token);
    }

    @Override
    public void singUp(SingUpRequestDTO request, MultipartFile avatar) {
        HttpEntity<MultiValueMap<String, Object>> body = httpHelper.buildMultiPartHttpEntity(Map.of(
                "avatar", List.of(avatar.getResource()), "user", List.of(request)
        ));

        httpHelper.doPost(userServerUrl + "/singup", body, String.class);
    }
}
