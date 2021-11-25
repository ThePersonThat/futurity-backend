package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.entity.User;

import java.io.IOException;

public interface AuthService {
    User singUp(SingUpRequestDTO request) throws IOException;
    LoginResponseDTO login(LoginRequestDTO request);
}
