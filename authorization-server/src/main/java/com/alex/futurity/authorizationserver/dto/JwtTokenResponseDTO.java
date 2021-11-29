package com.alex.futurity.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenResponseDTO {
    private String token;
}
