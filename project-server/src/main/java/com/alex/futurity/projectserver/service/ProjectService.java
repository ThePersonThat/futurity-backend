package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.ProjectPreviewRequestDTO;
import com.alex.futurity.projectserver.entity.Project;

import java.util.List;

public interface ProjectService {
    void saveProject(Project project);
    boolean hasUserProjectWithName(String name, long userId);
    List<Project> getProjectsForUser(long userId);
    byte[] getPreviewForUserProject(ProjectPreviewRequestDTO dto);
}
