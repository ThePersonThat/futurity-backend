package com.alex.futurity.projectserver.dto;

import com.alex.futurity.projectserver.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskDto {
    private long id;
    private String name;
    private long columnId;
    private String deadline;

    public static TaskDto fromTask(Task task) {
        return new TaskDto(task.getId(), task.getName(), task.getColumn().getId(), task.getDeadline());
    }
}
