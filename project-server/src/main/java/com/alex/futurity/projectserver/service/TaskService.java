package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.ChangeTaskIndexRequestDto;

public interface TaskService {
    long createTask(long userId, long projectId, int columnIndex, String taskName);
    void deleteTask(long userId, long projectId, int columnIndex, int taskIndex);
    void changeTaskIndex(long userId, long projectId, ChangeTaskIndexRequestDto request);
    void changeTaskName(long userId, long projectId, int columnIndex, int taskIndex, String taskName);
}
