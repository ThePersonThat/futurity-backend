package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RegistrationUserService {
    void registerUser(SingUpRequestDTO request, MultipartFile avatar);
}

