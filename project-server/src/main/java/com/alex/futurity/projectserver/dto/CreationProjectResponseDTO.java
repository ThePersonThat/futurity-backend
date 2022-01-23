package com.alex.futurity.projectserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CreationProjectResponseDTO {
    private final long projectId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CreationProjectResponseDTO(@JsonProperty("projectId") long projectId) {
        this.projectId = projectId;
    }
}
