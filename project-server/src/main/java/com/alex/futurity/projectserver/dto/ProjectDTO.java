package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectDTO {
    private String name;
    private String description;
    private String previewUrl;

    public ProjectDTO(Project project) {
        this.name = project.getName();
        this.description = project.getDescription();
        this.previewUrl = "/preview/" + project.getId();
    }
}
