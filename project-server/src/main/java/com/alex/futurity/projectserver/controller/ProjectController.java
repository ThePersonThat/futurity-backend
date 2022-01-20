package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.CreationProjectRequestDTO;
import com.alex.futurity.projectserver.dto.ProjectsResponseDTO;
import com.alex.futurity.projectserver.service.ProjectManagerService;
import com.alex.futurity.projectserver.validation.FileNotEmpty;
import com.alex.futurity.projectserver.validation.FileSize;
import com.alex.futurity.projectserver.validation.FileType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
public class ProjectController {
    private final ProjectManagerService projectService;

    @PostMapping("/{id}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@PathVariable long id, @RequestPart @FileNotEmpty(message = "Avatar must not be empty")
    @FileSize(max = 5 * (1024 * 1024),
            message = "Avatar is too large. Max size 5MB")
    @FileType(types = {"jpeg", "jpg", "png", "gif"},
            message = "Wrong image type. " +
                    "Must be one of the following: .jpeg, .png, .gif") MultipartFile preview, @Valid @RequestPart CreationProjectRequestDTO project) {
        project.setUserId(id);
        project.setPreview(preview);

        projectService.createProject(project);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<ProjectsResponseDTO> getProjects(@PathVariable long id) {
        return ResponseEntity.ok(projectService.getProjects(id));
    }
}
