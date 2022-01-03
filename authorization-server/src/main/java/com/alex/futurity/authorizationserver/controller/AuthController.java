package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.dto.*;
import com.alex.futurity.authorizationserver.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/singup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void singUp(@RequestPart MultipartFile avatar, @Valid @RequestPart SingUpRequestDTO user) {
        service.singUp(user, avatar);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO user) {
        return ResponseEntity.ok(service.login(user));
    }

    @PostMapping("/confirm-code")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmCode(@Valid @RequestBody ConfirmCodeRequestDTO code) {
        service.confirmCode(code);
    }

    @PostMapping("/confirm-email")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmEmail(@Valid @RequestBody ConfirmEmailRequestDTO email) {
        service.confirmEmail(email);
    }
}
