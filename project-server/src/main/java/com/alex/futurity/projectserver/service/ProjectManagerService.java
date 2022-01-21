package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.CreationProjectRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectPreviewRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectsResponseDTO;
import org.springframework.core.io.Resource;

public interface ProjectManagerService {
    void createProject(CreationProjectRequestDTO dto);
    ProjectsResponseDTO getProjects(long id);
    Resource findProjectPreview(ProjectPreviewRequestDTO dto);
}
