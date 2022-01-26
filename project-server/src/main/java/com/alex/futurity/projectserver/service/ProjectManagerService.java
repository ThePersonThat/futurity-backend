package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.*;
import org.springframework.core.io.Resource;

public interface ProjectManagerService {
    IdResponse createProject(CreationProjectRequestDTO dto);
    ListResponse<ProjectDTO> getProjects(long id);
    Resource findProjectPreview(TwoIdRequestDTO dto);
    void deleteProject(TwoIdRequestDTO dto);
}
