package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.*;
import com.alex.futurity.projectserver.entity.Project;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.repo.ProjectRepository;
import com.alex.futurity.projectserver.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepo;

    @Override
    @Transactional
    public long createProject(CreationProjectRequestDTO dto) {
        try {
            if (hasUserProjectWithName(dto.getName(), dto.getUserId())) {
                throw new ClientSideException("Project with such name exists", HttpStatus.CONFLICT);
            }

            Project project = dto.toProject();
            projectRepo.save(project);
            log.info("The project with name {} has been saved for user with {} id", project.getName(), project.getUserId());
            return project.getId();
        } catch (IOException e) {
            log.warn("The preview {} cannot be read: {}", dto.getPreview(), e.getMessage());
            throw new IllegalStateException("The preview cannot be read");
        }
    }

    @Override
    @Transactional
    public List<ProjectDTO> getProjects(long id) {
        List<Project> projects = projectRepo.findAllByUserId(id);
        List<ProjectDTO> dtos = projects.stream().map(ProjectDTO::new).collect(Collectors.toList());

        return dtos;
    }

    @Override
    @Transactional
    public Resource findProjectPreview(long userId, long projectId) {
        Project project = findByProjectIdAndUserId(projectId, userId);

        return new ByteArrayResource(project.getPreview());
    }

    @Override
    @Transactional
    public void deleteProject(long userId, long projectId) {
        int deleted = projectRepo.deleteProjectByIdAndUserId(userId, projectId);

        if (deleted == 0) {
            throw new ClientSideException("The project is associated with such data does not exist", HttpStatus.NOT_FOUND);
        }
    }

    private boolean hasUserProjectWithName(String name, long userId) {
        return projectRepo.findByNameAndUserId(name, userId).isPresent();
    }

    private Project findByProjectIdAndUserId(long projectId, long userId) {
        return projectRepo.findByIdAndUserId(projectId, userId).orElseThrow(() -> new ClientSideException(
                "The project is associated with such data does not exist", HttpStatus.NOT_FOUND)
        );
    }
}
