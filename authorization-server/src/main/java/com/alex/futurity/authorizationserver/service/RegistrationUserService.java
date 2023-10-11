package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.client.UserServiceClient;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationUserService {
    private final UserServiceClient userServiceClient;

    public void registerUser(SingUpRequestDto request, MultipartFile avatar) {
        userServiceClient.singUp(avatar, request);
    }
}
