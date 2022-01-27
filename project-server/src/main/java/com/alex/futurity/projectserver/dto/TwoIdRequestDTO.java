package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TwoIdRequestDTO {
    private final long firstId;
    private final long secondId;
}
