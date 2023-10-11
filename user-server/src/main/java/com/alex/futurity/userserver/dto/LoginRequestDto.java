package com.alex.futurity.userserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @NotBlank(message = "Wrong password. Password must not be empty")
    @Size(min = 6, max = 64, message = "Wrong password. Password must be more than 6 and less 64 characters")
    private String password;
}
