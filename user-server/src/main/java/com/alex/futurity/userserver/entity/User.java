package com.alex.futurity.userserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 4, max = 64)
    private String nickname;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 6, max = 64)
    private String password;

    @NotNull
    @Lob
    private byte[] avatar;

    public User(String email, String nickname, String password, byte[] avatar) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.avatar = avatar;
    }
}
