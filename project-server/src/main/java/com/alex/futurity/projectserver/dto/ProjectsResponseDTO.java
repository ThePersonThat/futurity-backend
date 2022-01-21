package com.alex.futurity.projectserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectsResponseDTO {
    private final List<ProjectDTO> projects;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProjectsResponseDTO(@JsonProperty("projects") List<ProjectDTO> projects) {
        this.projects = projects;
    }
}
