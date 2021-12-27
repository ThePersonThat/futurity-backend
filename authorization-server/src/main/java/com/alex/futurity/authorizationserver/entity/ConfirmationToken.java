package com.alex.futurity.authorizationserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Wrong code. Code must no be empty")
    @Size(max = 6, min = 6, message = "Wrong code. Code must be 6 characters")
    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @NotBlank(message = "Wrong email. Email must not be empty")
    @Email(message = "Wrong email. Correct pattern: emailName@email.com")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean confirmed = false;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime confirmedAt;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public ConfirmationToken(String code, String email, LocalDateTime expiredAt) {
        this.code = code;
        this.email = email;
        this.confirmedAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
    }
}
