package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.dto.LoginRequestDTO;
import com.alex.futurity.userserver.dto.LoginResponseDTO;
import com.alex.futurity.userserver.dto.SingUpRequestDTO;
import com.alex.futurity.userserver.dto.UserExistRequestDTO;
import com.alex.futurity.userserver.service.AuthService;
import com.alex.futurity.userserver.validation.FileNotEmpty;
import com.alex.futurity.userserver.validation.FileSize;
import com.alex.futurity.userserver.validation.FileType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService userService) {
        this.authService = userService;
    }

    @PostMapping(value = "/singup")
    public ResponseEntity<String> singUpUser(@RequestPart @FileNotEmpty(message = "Avatar must not be empty")
                                             @FileSize(max = 5 * (1024 * 1024),
                                                     message = "Avatar is too large. Max size 5MB")
                                             @FileType(types = {"jpeg", "jpg", "png", "gif"},
                                                     message = "Wrong image type. " +
                                                             "Must be one of the following: .jpeg, .png, .gif")
                                                     MultipartFile avatar,
                                             @Valid @RequestPart SingUpRequestDTO user) {
        user.setAvatar(avatar);
        authService.singUp(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/exist")
    public ResponseEntity<Boolean> exist(@Valid @RequestBody UserExistRequestDTO request) {
        boolean userExist = authService.isUserExist(request);
        return ResponseEntity.ok().body(userExist);
    }
}
