package com.alex.futurity.projectserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Wrong name. Name must not be empty")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Wrong description. Description must not be empty")
    private String description;

    @NotNull(message = "Project preview must not be null")
    @Lob
    private byte[] preview;

    public Project(Long userId, String name, String description, byte[] preview) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.preview = preview;
    }
}
