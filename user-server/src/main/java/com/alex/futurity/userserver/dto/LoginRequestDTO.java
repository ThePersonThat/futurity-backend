package com.alex.futurity.userserver.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequestDTO {
    @Email
    private String email;

    @Size(min = 6, max = 64)
    private String password;
}
