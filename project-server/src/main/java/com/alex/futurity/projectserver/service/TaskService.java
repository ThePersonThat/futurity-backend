package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.ChangeTaskIndexRequestDto;
import com.alex.futurity.projectserver.dto.CreationTaskDto;

public interface TaskService {
    long createTask(long userId, long projectId, long columnId, CreationTaskDto creationTaskDto);
    void deleteTask(long userId, long projectId, long columnId, long taskId);
    void changeTaskIndex(long userId, long projectId, ChangeTaskIndexRequestDto request);
    void changeTaskName(long userId, long projectId, long columnId, long taskId, String taskName);
}
