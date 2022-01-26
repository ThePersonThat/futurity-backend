package com.alex.futurity.projectserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> {
    private final List<T> values;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ListResponse(@JsonProperty("values") List<T> values) {
        this.values = values;
    }
}
