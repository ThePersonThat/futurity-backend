package com.alex.futurity.projectserver.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ProjectColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Wrong name. Name must not be empty")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Wrong index number. Number must not be empty")
    @Min(value = 1, message = "Wrong index number. Index number must start from 1")
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "column")
    private List<Task> tasks;

    public ProjectColumn(String name, Integer index, Project project) {
        this.name = name;
        this.index = index;
        this.project = project;
    }

    public int getLastTaskIndex() {
        return tasks.size() + 1;
    }
}
