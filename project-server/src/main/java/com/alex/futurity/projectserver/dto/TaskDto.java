package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskDto {
    private String name;
    private int columnIndex;

    public static TaskDto fromTask(Task task) {
        return new TaskDto(task.getName(), task.getColumn().getIndex());
    }
}
