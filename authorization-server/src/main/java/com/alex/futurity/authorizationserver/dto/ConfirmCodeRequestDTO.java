package com.alex.futurity.authorizationserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
public class ConfirmCodeRequestDTO {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @NotBlank(message = "Wrong code. Code must no be empty")
    @Size(max = 6, min = 6, message = "Wrong code. Code must be 6 characters")
    @Column(nullable = false, unique = true, length = 6)
    private String code;
}
