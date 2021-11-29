package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.dto.JwtTokenResponseDTO;
import com.alex.futurity.authorizationserver.dto.LoginRequestDTO;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDTO;
import com.alex.futurity.authorizationserver.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/singup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void singUp(@RequestPart MultipartFile avatar, @Valid @RequestPart SingUpRequestDTO user) {
        service.singUp(user, avatar);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO user) {
        return ResponseEntity.ok(service.login(user));
    }
}
