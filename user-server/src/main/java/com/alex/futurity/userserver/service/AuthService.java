package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;

import java.io.IOException;

public interface AuthService {
    void singUp(SingUpRequestDTO request) throws IOException;
    LoginResponseDTO login(LoginRequestDTO request);
}
