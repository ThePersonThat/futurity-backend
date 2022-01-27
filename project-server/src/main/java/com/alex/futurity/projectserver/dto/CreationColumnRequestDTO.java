package com.alex.futurity.projectserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
public class CreationColumnRequestDTO {
    @NotBlank(message = "Wrong name. Name must not be empty")
    private String name;
    @Null
    private Long userId;
    @Null
    private Long projectId;
}
