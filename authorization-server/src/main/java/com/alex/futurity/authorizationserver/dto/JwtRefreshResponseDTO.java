package com.alex.futurity.authorizationserver.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JwtRefreshResponseDTO {
    @NotNull
    private final String accessToken;
    @NotNull
    private final String refreshToken;
    @NotNull
    private final int age;
}
