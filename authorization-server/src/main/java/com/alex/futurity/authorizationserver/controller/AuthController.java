package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.dto.*;
import com.alex.futurity.authorizationserver.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private final AuthService service;

    @PostMapping("/singup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void singUp(@RequestPart MultipartFile avatar, @Valid @RequestPart SingUpRequestDto user) {
        service.singUp(user, avatar);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginRequestDto user, HttpServletResponse response) {
        JwtRefreshResponseDto dto = service.login(user);

        Cookie cookie = new Cookie(REFRESH_TOKEN_KEY, dto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(dto.getAge());

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtTokenDto(dto.getAccessToken()));
    }

    @PostMapping("/confirm-code")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmCode(@Valid @RequestBody ConfirmCodeRequestDto code) {
        service.confirmCode(code);
    }

    @PostMapping("/confirm-email")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmEmail(@Valid @RequestBody ConfirmEmailRequestDto email) {
        service.confirmEmail(email);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<JwtTokenDto> refreshToken(@CookieValue(REFRESH_TOKEN_KEY) String refreshToken) {
        return ResponseEntity.ok(service.refreshToken(new JwtTokenDto(refreshToken)));
    }
}
