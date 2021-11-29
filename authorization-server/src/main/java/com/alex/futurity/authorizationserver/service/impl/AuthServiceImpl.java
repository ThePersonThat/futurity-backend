package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.JwtTokenResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import com.alex.futurity.authorizationserver.service.AuthService;
import com.alex.futurity.authorizationserver.service.JwtService;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final RestTemplate restTemplate;
    private final HttpHelper httpHelper;
    private final JwtService jwtService;

    @Value("${user-server}")
    private String userServerUrl;

    public AuthServiceImpl(RestTemplate restTemplate, HttpHelper httpHelper, JwtService jwtService) {
        this.restTemplate = restTemplate;
        this.httpHelper = httpHelper;
        this.jwtService = jwtService;
    }

    @Override
    public JwtTokenResponseDTO login(LoginRequestDTO dto) {
        LoginDomain login = Optional.ofNullable(restTemplate.postForObject(userServerUrl + "/login", dto, LoginDomain.class))
                .orElseThrow(() -> {
                    log.warn("Error getting user from the user server");
                    throw new IllegalStateException("Registration error. Try again after a while");
                });
        String token = jwtService.generateAccessToken(login.getId());

        return new JwtTokenResponseDTO(token);
    }

    @Override
    public void singUp(SingUpRequestDTO request, MultipartFile avatar) {
        HttpEntity<MultiValueMap<String, Object>> body = httpHelper.buildMultiPartHttpEntity(Map.of(
                "avatar", List.of(avatar.getResource()), "user", List.of(request)
        ));

        restTemplate.postForObject(userServerUrl + "/singup", body, String.class);
    }
}
