package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDTO {
    private long id;
    private String name;
    private String description;
    private String previewUrl;

    public static ProjectDTO fromProject(Project project) {
        String previewUrl = "/preview/" + project.getId();

        return new ProjectDTO(project.getId(), project.getName(), project.getDescription(),
                previewUrl);
    }
}
