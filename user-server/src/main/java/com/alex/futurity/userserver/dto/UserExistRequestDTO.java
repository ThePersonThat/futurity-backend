package com.alex.futurity.userserver.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class UserExistRequestDTO {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserExistRequestDTO(String email) {
        this.email = email;
    }
}
