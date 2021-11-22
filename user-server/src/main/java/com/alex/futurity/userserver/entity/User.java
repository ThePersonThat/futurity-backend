package com.alex.futurity.userserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 4, max = 64)
    private String nickname;

    @NotNull
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
