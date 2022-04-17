package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.ProjectColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectColumnDto {
    private String name;
    private Integer index;

    public static ProjectColumnDto fromProjectColumn(ProjectColumn projectColumn) {
        return new ProjectColumnDto(projectColumn.getName(), projectColumn.getIndex());
    }
}
