package com.alex.futurity.projectserver.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ErrorMessage {
    private final String message;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ErrorMessage(@JsonProperty("message") String message) {
        this.message = message;
    }
}
