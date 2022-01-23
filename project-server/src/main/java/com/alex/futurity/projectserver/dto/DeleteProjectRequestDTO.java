package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteProjectRequestDTO {
    private long userId;
    private long projectId;
}
