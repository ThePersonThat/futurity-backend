package com.alex.futurity.projectserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IdResponse {
    private final long id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IdResponse(@JsonProperty("id") long id) {
        this.id = id;
    }
}
