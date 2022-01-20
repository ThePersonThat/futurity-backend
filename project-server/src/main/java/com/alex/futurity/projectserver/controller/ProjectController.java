package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.CreationProjectDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProjectController {
    @PostMapping("/{id}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@PathVariable long id, @RequestPart MultipartFile preview, @RequestPart CreationProjectDTO project) {
        project.setUserId(id);
        project.setPreview(preview);
    }
}
