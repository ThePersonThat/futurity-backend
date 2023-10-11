package com.alex.futurity.userserver.dto;

import com.alex.futurity.userserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class LoginResponseDto {
    private Long id;

    @Size(min = 4, max = 64, message = "Wrong nickname. Nickname must be more than 4 and less 64 characters")
    private String nickname;

    public LoginResponseDto(User user) {
        id = user.getId();
        nickname = user.getNickname();
    }
}
