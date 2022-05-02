package com.alex.futurity.projectserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class RequestStringDto {
    @NotEmpty(message = "Value must not be null")
    @NotBlank(message = "Value must not be null")
    @NotNull(message = "Value must not be null")
    private String value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RequestStringDto(@JsonProperty("value") String value) {
        this.value = value;
    }
}
