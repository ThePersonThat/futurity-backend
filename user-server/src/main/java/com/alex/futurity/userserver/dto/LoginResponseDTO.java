package com.alex.futurity.userserver.dto;

import com.alex.futurity.userserver.entity.User;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class LoginResponseDTO {
    private Long id;

    @Size(min = 4, max = 64, message = "Wrong nickname. Nickname must be more than 4 and less 64 characters")
    private String nickname;

    public LoginResponseDTO(User user) {
        id = user.getId();
        nickname = user.getNickname();
    }
}
