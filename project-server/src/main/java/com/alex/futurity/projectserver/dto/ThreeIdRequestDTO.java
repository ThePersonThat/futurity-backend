package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ThreeIdRequestDTO {
    private final long firstId;
    private final long secondId;
    private final long thirdId;
}
