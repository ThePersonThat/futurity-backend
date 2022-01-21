package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
public class CreationProjectRequestDTO {
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
