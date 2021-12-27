package com.alex.futurity.authorizationserver.service.impl;

import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import com.alex.futurity.authorizationserver.service.ConfirmationTokenService;
import com.alex.futurity.authorizationserver.service.EmailSenderService;
import com.alex.futurity.authorizationserver.service.RegistrationUserService;
import com.alex.futurity.authorizationserver.utils.HttpHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class RegistrationUserServiceImpl implements RegistrationUserService {
    private final HttpHelper httpHelper;
    private final EmailSenderService emailSender;
    private final ConfirmationTokenService tokenService;

    @Value("${user-server}")
    private String userServerUrl;
    private String url;

    public RegistrationUserServiceImpl(HttpHelper httpHelper, EmailSenderService emailSender, ConfirmationTokenService tokenService) {
        this.httpHelper = httpHelper;
        this.emailSender = emailSender;
        this.tokenService = tokenService;
    }

    @PostConstruct
    private void setupUrl() {
        this.url = userServerUrl + "/singup";
    }

    @Override
    public void registerUser(SingUpRequestDTO request, MultipartFile avatar) {
        HttpEntity<MultiValueMap<String, Object>> body = httpHelper.buildMultiPartHttpEntity(Map.of(
                "avatar", List.of(avatar.getResource()), "user", List.of(request)
        ));

        httpHelper.doPost(url, body, String.class);
    }
}
