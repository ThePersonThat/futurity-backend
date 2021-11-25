package com.alex.futurity.userserver.dto;

import com.alex.futurity.userserver.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginResponseDTO {
    private Long id;

    @NotNull(message = "Wrong nickname. Nickname must not be null")
    @NotEmpty(message = "Wrong nickname. Nickname must not be empty")
    @NotBlank(message = "Wrong nickname. Nickname must not be empty")
    @Size(min = 4, max = 64, message = "Wrong nickname. Nickname must be more than 4 and less 64 characters")
    private String nickname;

    public LoginResponseDTO(User user) {
        id = user.getId();
        nickname = user.getNickname();
    }
}
