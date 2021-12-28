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
    public ResponseEntity<String> confirmCode(@Valid @RequestBody ConfirmCodeRequestDTO code) {
        service.confirmCode(code);

        return ResponseEntity.ok().body("Confirmed");
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@Valid @RequestBody ConfirmEmailRequestDTO email) {
        service.confirmEmail(email);

        return ResponseEntity.ok().body("The confirmation message is send");
    }
}
