package com.alex.futurity.userserver.dto;

import com.alex.futurity.userserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import java.io.IOException;

@Data
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

    @Null
    private MultipartFile avatar;

    public User toUser() throws IOException {
        return new User(email, nickname, password, avatar.getBytes());
    }
}
