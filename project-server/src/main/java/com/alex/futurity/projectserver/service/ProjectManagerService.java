package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.CreationProjectRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectsResponseDTO;

public interface ProjectManagerService {
    void createProject(CreationProjectRequestDTO dto);
    ProjectsResponseDTO getProjects(long id);
}
