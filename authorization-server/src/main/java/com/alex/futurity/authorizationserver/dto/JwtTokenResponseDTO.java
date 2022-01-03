package com.alex.futurity.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class JwtTokenResponseDTO {
    @NotNull
    private final String token;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public JwtTokenResponseDTO(@JsonProperty("token") String token) {
        this.token = token;
    }
}
