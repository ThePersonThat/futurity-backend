package com.alex.futurity.projectserver.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProjectsResponseDTO {
    private final List<ProjectDTO> projects;
}
