package com.alex.futurity.authorizationserver.domain;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginDomain {
    private Long id;

    @Size(min = 4, max = 64, message = "Wrong nickname. Nickname must be more than 4 and less 64 characters")
    private String nickname;
}
