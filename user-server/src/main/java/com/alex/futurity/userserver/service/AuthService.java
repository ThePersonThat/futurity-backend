package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.entity.User;

public interface AuthService {
    User singUp(SingUpRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
}
