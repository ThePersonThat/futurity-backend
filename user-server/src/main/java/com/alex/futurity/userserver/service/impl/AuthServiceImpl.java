package com.alex.futurity.userserver.service.impl;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.UserAlreadyExistException;
import com.alex.futurity.userserver.exception.UserNotFoundException;
import com.alex.futurity.userserver.service.AuthService;
import com.alex.futurity.userserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@AllArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public User singUp(SingUpRequestDTO request) throws IOException {
        if (userService.isUserExist(request.getEmail())) {
            throw new UserAlreadyExistException("Error. A user with the same email address already exists");
        }

        User user = request.toUser();
        user.setPassword(encoder.encode(user.getPassword()));

        userService.saveUser(user);

        log.info("User {} have been registered", user);
        return user;
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userService.findUserByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException();
        }

        return new LoginResponseDTO(user);
    }
}
