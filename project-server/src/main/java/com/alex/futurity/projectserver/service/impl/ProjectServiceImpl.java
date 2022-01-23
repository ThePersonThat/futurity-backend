package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.DeleteProjectRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectPreviewRequestDTO;
import com.alex.futurity.projectserver.entity.Project;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.repo.ProjectRepository;
import com.alex.futurity.projectserver.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepo;

    @Override
    public void saveProject(Project project) {
        projectRepo.save(project);
    }

    @Override
    public boolean hasUserProjectWithName(String name, long userId) {
        return projectRepo.findByNameAndUserId(name, userId).isPresent();
    }

    @Override
    public List<Project> getProjectsForUser(long userId) {
        return projectRepo.findAllByUserId(userId);
    }

    @Override
    public byte[] getPreviewForUserProject(ProjectPreviewRequestDTO dto) {
        Project project = projectRepo.findByIdAndUserId(dto.getReviewId(), dto.getUserId()).orElseThrow(() -> {
            throw new ClientSideException("The project is associated with such data does not exist", HttpStatus.NOT_FOUND);
        });

        return project.getPreview();
    }

    @Override
    public int deleteProject(DeleteProjectRequestDTO dto) {
        return projectRepo.deleteProjectByIdAndUserId(dto.getProjectId(), dto.getUserId());
    }
}
