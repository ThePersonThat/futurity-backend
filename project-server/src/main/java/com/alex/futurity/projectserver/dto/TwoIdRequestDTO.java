package com.alex.futurity.projectserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TwoIdRequestDTO {
    private long firstId;
    private long secondId;
}
