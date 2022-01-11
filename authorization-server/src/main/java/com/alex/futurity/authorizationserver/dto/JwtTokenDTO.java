package com.alex.futurity.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
public class JwtTokenDTO {
    @NotNull
    @NotEmpty
    @NotBlank
    private final String token;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public JwtTokenDTO(@JsonProperty("token") String token) {
        this.token = token;
    }
}
