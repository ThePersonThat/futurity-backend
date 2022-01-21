package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.CreationProjectRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectDTO;
import com.alex.futurity.projectserver.dto.ProjectPreviewRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectsResponseDTO;
import com.alex.futurity.projectserver.entity.Project;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.service.ProjectManagerService;
import com.alex.futurity.projectserver.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProjectManagerServiceImpl implements ProjectManagerService {
    private final ProjectService projectService;

    @Override
    @Transactional
    public void createProject(CreationProjectRequestDTO dto) {
        try {
            if (projectService.hasUserProjectWithName(dto.getName(), dto.getUserId())) {
                throw new ClientSideException("Project with such name exists", HttpStatus.CONFLICT);
            }

            Project project = dto.toProject();
            projectService.saveProject(project);
            log.info("The project with name {} has been saved for user with {} id", project.getName(), project.getUserId());
        } catch (IOException e) {
            log.warn("The preview {} cannot be read: {}", dto.getPreview(), e.getMessage());
            throw new IllegalStateException("The preview cannot be read");
        }
    }

    @Override
    @Transactional
    public ProjectsResponseDTO getProjects(long id) {
        List<Project> projects = projectService.getProjectsForUser(id);
        List<ProjectDTO> dtos = projects.stream().map(ProjectDTO::new).collect(Collectors.toList());

        return new ProjectsResponseDTO(dtos);
    }

    @Override
    @Transactional
    public Resource findProjectPreview(ProjectPreviewRequestDTO dto) {
        byte[] preview = projectService.getPreviewForUserProject(dto);

        return new ByteArrayResource(preview);
    }
}
