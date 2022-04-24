package com.alex.futurity.projectserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Wrong name. Name must not be empty")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Wrong index number. Number must not be empty")
    @Min(value = 0, message = "Wrong index number. Index number must start from 0")
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "column_id", nullable = false)
    private ProjectColumn column;

    public Task(String name, ProjectColumn column) {
        this.name = name;
        this.column = column;
    }
}
