package com.alex.futurity.userserver.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
public class UserExistRequestDto {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserExistRequestDto(String email) {
        this.email = email;
    }
}
