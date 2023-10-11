package com.alex.futurity.authorizationserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtTokenDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private final String token;
}
