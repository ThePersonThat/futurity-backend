package com.alex.futurity.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ConfirmEmailRequestDTO {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ConfirmEmailRequestDTO(String email) {
        this.email = email;
    }
}
