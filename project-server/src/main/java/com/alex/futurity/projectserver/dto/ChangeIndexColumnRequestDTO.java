package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Setter
@Getter
public class ChangeIndexColumnRequestDTO {
    @NotNull
    @Min(1)
    private final int from;
    @NotNull
    @Min(1)
    private final int to;

    private long projectId;
    private long userId;
}
