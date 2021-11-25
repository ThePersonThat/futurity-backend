package com.alex.futurity.userserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class LoginRequestDTO {
    @NotNull(message = "Wrong email. Email must not be null")
    @NotEmpty(message = "Wrong email. Email must not be empty")
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @NotNull(message = "Wrong password. Password must not be null")
    @NotEmpty(message = "Wrong password. Password must not be empty")
    @NotBlank(message = "Wrong password. Password must not be empty")
    @Size(min = 6, max = 64, message = "Wrong password. Password must be more than 6 and less 64 characters")
    private String password;
}
