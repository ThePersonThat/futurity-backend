package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.dto.JwtRefreshResponseDTO;
import com.alex.futurity.authorizationserver.service.oauth.EmailPasswordOauthLogin;
import com.alex.futurity.authorizationserver.service.oauth.FuturityOauthLoginHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/oauth/")
@Log4j2
public class OauthController {
    private final FuturityOauthLoginHandler oauthLoginHandler;

    public OauthController(@Qualifier("emailPasswordFuturityOauthLoginHandler") FuturityOauthLoginHandler oauthLoginHandler) {
        this.oauthLoginHandler = oauthLoginHandler;
    }

    @RequestMapping("/login/email/password")
    public ResponseEntity<?> loginUser(@RequestBody EmailPasswordOauthLogin dto, @RequestParam String redirectUrl, HttpServletResponse response) throws IOException {
        log.info("REDIRECT URL: {}", redirectUrl);
        JwtRefreshResponseDTO jwtRefreshResponseDTO = this.oauthLoginHandler.loginUser(dto);
        String url = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("accessToken", jwtRefreshResponseDTO.getAccessToken())
                .queryParam("refreshToken", jwtRefreshResponseDTO.getRefreshToken()).toUriString();//todo return tokens in cookies(?)
        response.sendRedirect(url);
        return ResponseEntity.ok(jwtRefreshResponseDTO);
    }
}
