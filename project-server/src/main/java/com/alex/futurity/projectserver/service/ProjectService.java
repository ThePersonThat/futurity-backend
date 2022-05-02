package com.alex.futurity.projectserver.service;


import com.alex.futurity.projectserver.dto.*;
import com.alex.futurity.projectserver.entity.ProjectColumn;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ProjectService {
    long createProject(CreationProjectRequestDTO dto);
    List<ProjectDTO> getProjects(long userId);
    Resource findProjectPreview(long userId, long projectId);
    void deleteProject(long userId, long projectId);
    ProjectColumn addColumnToProject(long userId, long projectId, String columnName);
    void changeProjectName(long userId, long projectId, String projectName);
    void changeProjectDescription(long userId, long projectId, String projectDescription);
}
