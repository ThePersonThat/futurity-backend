package com.alex.futurity.userserver.dto;

import com.alex.futurity.userserver.entity.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.io.IOException;

@Data
public class SingUpRequestDTO {

    @NotNull
    @NotEmpty
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 4, max = 64)
    private String nickname;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 6, max = 64, message = "Password should be more than 6 chars")
    private String password;

    @Null
    private MultipartFile avatar;

    public User toUser() throws IOException {
        return new User(email, nickname, password, avatar.getBytes());
    }
}
