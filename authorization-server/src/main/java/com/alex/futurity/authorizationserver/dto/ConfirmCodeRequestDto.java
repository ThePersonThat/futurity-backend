package com.alex.futurity.authorizationserver.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ConfirmCodeRequestDto {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @NotBlank(message = "Wrong code. Code must no be empty")
    @Size(max = 6, min = 6, message = "Wrong code. Code must be 6 characters")
    @Column(nullable = false, unique = true, length = 6)
    private String code;
}
