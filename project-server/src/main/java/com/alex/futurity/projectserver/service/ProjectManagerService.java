package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.*;
import org.springframework.core.io.Resource;

public interface ProjectManagerService {
    CreationProjectResponseDTO createProject(CreationProjectRequestDTO dto);
    ProjectsResponseDTO getProjects(long id);
    Resource findProjectPreview(ProjectPreviewRequestDTO dto);
    void deleteProject(DeleteProjectRequestDTO dto);
}
