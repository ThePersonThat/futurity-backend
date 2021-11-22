package com.alex.futurity.userserver.dto;

import com.alex.futurity.userserver.entity.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginResponseDTO {
    private Long id;

    @Size(min = 4, max = 64)
    private String nickname;

    public LoginResponseDTO(User user) {
        id = user.getId();
        nickname = user.getNickname();
    }
}
