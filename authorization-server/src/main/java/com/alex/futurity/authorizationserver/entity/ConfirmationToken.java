package com.alex.futurity.authorizationserver.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Data
@Accessors(chain = true)
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

    private ZonedDateTime confirmedAt;

    @NotNull
    @Column(nullable = false)
    private ZonedDateTime expiredAt;

    public ConfirmationToken(String code, String email, ZonedDateTime expiredAt) {
        this.code = code;
        this.email = email;
        this.expiredAt = expiredAt;
    }
}
