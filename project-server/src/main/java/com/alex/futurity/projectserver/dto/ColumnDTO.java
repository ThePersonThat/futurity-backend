package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.ProjectColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ColumnDTO {
    private final long id;
    private final String name;
    private final int index;

    public ColumnDTO(ProjectColumn column) {
        this.id = column.getId();
        this.name = column.getName();
        this.index = column.getIndex();
    }
}
