package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.*;
import com.alex.futurity.projectserver.service.ProjectManagerService;
import com.alex.futurity.projectserver.validation.FileNotEmpty;
import com.alex.futurity.projectserver.validation.FileSize;
import com.alex.futurity.projectserver.validation.FileType;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<CreationProjectResponseDTO> createProject(@PathVariable long id, @RequestPart @FileNotEmpty(message = "Preview must not be empty")
    @FileSize(max = 5 * (1024 * 1024),
            message = "Preview is too large. Max size 5MB")
    @FileType(types = {"jpeg", "jpg", "png", "gif"},
            message = "Wrong image type. " +
                    "Must be one of the following: .jpeg, .png, .gif") MultipartFile preview, @Valid @RequestPart CreationProjectRequestDTO project) {
        project.setUserId(id);
        project.setPreview(preview);

        return new ResponseEntity<>(projectService.createProject(project), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<ProjectsResponseDTO> getProjects(@PathVariable long id) {
        return ResponseEntity.ok(projectService.getProjects(id));
    }

    @GetMapping(value = "/{id}/preview/{previewId}", produces = {
            MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE
    })
    public ResponseEntity<Resource> getPreview(@PathVariable long id, @PathVariable long previewId) {
        ProjectPreviewRequestDTO request = new ProjectPreviewRequestDTO(id, previewId);

        return ResponseEntity.ok(projectService.findProjectPreview(request));
    }

    @DeleteMapping("/{id}/delete/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable long id, @PathVariable long projectId) {
        DeleteProjectRequestDTO dto = new DeleteProjectRequestDTO(id, projectId);

        projectService.deleteProject(dto);
    }
}
