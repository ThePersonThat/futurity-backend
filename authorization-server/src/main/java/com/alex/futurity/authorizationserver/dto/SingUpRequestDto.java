package com.alex.futurity.authorizationserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingUpRequestDto {
    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    private String email;

    @NotBlank(message = "Wrong nickname. Nickname must not be empty")
    @Size(min = 4, max = 64, message = "Wrong nickname. Nickname must be more than 4 and less 64 characters")
    private String nickname;

    @NotBlank(message = "Wrong password. Password must not be empty")
    @Size(min = 6, max = 64, message = "Wrong password. Password must be more than 6 and less 64 characters")
    private String password;
}
