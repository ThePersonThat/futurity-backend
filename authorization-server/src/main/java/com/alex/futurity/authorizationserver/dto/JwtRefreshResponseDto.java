package com.alex.futurity.authorizationserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRefreshResponseDto {
    @NotNull
    private final String accessToken;
    @NotNull
    private final String refreshToken;
    @NotNull
    private final int age;
}
