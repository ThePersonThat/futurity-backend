package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.dto.LoginRequestDto;
import com.alex.futurity.userserver.dto.SingUpRequestDto;
import com.alex.futurity.userserver.dto.LoginResponseDto;
import com.alex.futurity.userserver.dto.UserExistRequestDto;
import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.CannotUploadFileException;
import com.alex.futurity.userserver.exception.ClientSideException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Transactional
    public User singUp(SingUpRequestDto request) {
        try {
            if (userService.isUserExist(request.getEmail())) {
                throw new ClientSideException("Error. A user with the same email address already exists", HttpStatus.CONFLICT);
            }
            User user = request.toUser();
            user.setPassword(encoder.encode(user.getPassword()));

            userService.saveUser(user);

            log.info("User {} have been registered", user);
            return user;
        } catch (IOException e) {
            log.warn("The avatar {} cannot be read: {}", request.getAvatar(), e.getMessage());
            throw new CannotUploadFileException("The avatar cannot be read");
        }
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userService.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new ClientSideException("Email or password is incorrect. Check the entered data", HttpStatus.NOT_FOUND));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new ClientSideException("Email or password is incorrect. Check the entered data", HttpStatus.NOT_FOUND);
        }

        log.info("The user with email {} was login in", request.getEmail());
        return new LoginResponseDto(user);
    }

    @Transactional
    public boolean isUserExist(UserExistRequestDto request) {
        return userService.findUserByEmail(request.getEmail()).isPresent();
    }
}
