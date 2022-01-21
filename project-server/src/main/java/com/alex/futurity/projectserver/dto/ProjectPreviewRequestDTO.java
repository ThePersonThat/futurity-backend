package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectPreviewRequestDTO {
    private long userId;
    private long reviewId;
}
