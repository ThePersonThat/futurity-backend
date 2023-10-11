package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.LoginRequestDto;
import com.alex.futurity.userserver.dto.LoginResponseDto;
import com.alex.futurity.userserver.dto.SingUpRequestDto;
import com.alex.futurity.userserver.dto.UserExistRequestDto;
import com.alex.futurity.userserver.service.AuthService;
import com.alex.futurity.userserver.validation.FileNotEmpty;
import com.alex.futurity.userserver.validation.FileSize;
import com.alex.futurity.userserver.validation.FileType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService userService) {
        this.authService = userService;
    }

    @PostMapping(value = "/singup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> singUpUser(@RequestPart @FileNotEmpty(message = "Avatar must not be empty")
                                             @FileSize(max = 5 * (1024 * 1024),
                                                     message = "Avatar is too large. Max size 5MB")
                                             @FileType(types = {"jpeg", "jpg", "png", "gif"},
                                                     message = "Wrong image type. " +
                                                             "Must be one of the following: .jpeg, .png, .gif")
                                                     MultipartFile avatar,
                                             @Valid @RequestPart SingUpRequestDto user) {
        user.setAvatar(avatar);
        authService.singUp(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/exist")
    public ResponseEntity<Boolean> exist(@Valid @RequestBody UserExistRequestDto request) {
        boolean userExist = authService.isUserExist(request);
        return ResponseEntity.ok().body(userExist);
    }
}
