package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.Project;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.IOException;

@Data
public class CreationProjectDTO {
    @NotBlank(message = "Wrong name. Name must not be empty")
    private String name;
    @NotBlank(message = "Wrong description. Description must not be empty")
    private String description;

    @Null
    private MultipartFile preview;
    @Null
    private Long userId;

    public Project toProject() throws IOException {
        return new Project(userId, name, description, preview.getBytes());
    }
}
