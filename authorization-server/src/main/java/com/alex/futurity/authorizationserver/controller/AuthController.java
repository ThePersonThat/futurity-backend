package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.dto.*;
import com.alex.futurity.authorizationserver.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private final AuthService service;

    @PostMapping("/singup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void singUp(@RequestPart MultipartFile avatar, @Valid @RequestPart SingUpRequestDTO user) {
        service.singUp(user, avatar);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> login(@Valid @RequestBody LoginRequestDTO user, HttpServletResponse response) {
        JwtRefreshResponseDTO dto = service.login(user);

        Cookie cookie = new Cookie(REFRESH_TOKEN_KEY, dto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(dto.getAge());

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtTokenDTO(dto.getAccessToken()));
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

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshResponseDTO> refreshToken(@CookieValue(REFRESH_TOKEN_KEY) String refreshToken) {
        return ResponseEntity.ok(service.refreshToken(new JwtTokenDTO(refreshToken)));
    }
}
