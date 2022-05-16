package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@ToString
public class CreationTaskDto {
    @NotEmpty(message = "Name must not be null")
    @NotBlank(message = "Name must not be null")
    @NotNull(message = "Name must not be null")
    private String name;
    private String deadline;
}
