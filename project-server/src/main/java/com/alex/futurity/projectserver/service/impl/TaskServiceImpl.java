package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.ChangeTaskIndexRequestDto;
import com.alex.futurity.projectserver.entity.Task;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.repo.TaskRepository;
import com.alex.futurity.projectserver.service.ColumnService;
import com.alex.futurity.projectserver.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final ColumnService columnService;
    private final TaskRepository taskRepo;

    @Override
    public long createTask(long userId, long projectId, int columnIndex, String taskName) {
        Task task = columnService.addTaskToColumn(userId, projectId, columnIndex, taskName);

        return task.getId();
    }

    @Override
    @Transactional
    public void deleteTask(long userId, long projectId, int columnIndex, int taskIndex) {
        List<Task> tasks = taskRepo.findAllTasks(userId, projectId, columnIndex);

        if (taskIndex < 0 || taskIndex > tasks.size()) {
            throw new ClientSideException("Tasks out of bounds", HttpStatus.BAD_REQUEST);
        }

        tasks.stream()
                .filter(task -> task.getIndex() > taskIndex)
                .forEach(task -> task.setIndex(task.getIndex() - 1));
        taskRepo.deleteTask(tasks.get(taskIndex).getId());
    }

    @Override
    @Transactional
    public void changeTaskIndex(long userId, long projectId, ChangeTaskIndexRequestDto request) {
        List<Task> columnFrom = taskRepo.findAllTasks(userId, projectId, request.getFromColumn());
        List<Task> columnTo = taskRepo.findAllTasks(userId, projectId, request.getToColumn());

        if (request.getFrom() < 0 || request.getTo() < 0
                || (request.getFromColumn() == request.getToColumn() && request.getFrom() == request.getTo())
                || request.getFrom() > columnFrom.size() || request.getTo() > columnTo.size() + 1) {
            throw new ClientSideException("Tasks out of bounds", HttpStatus.BAD_REQUEST);
        }

        Task task = columnFrom.get(request.getFrom());
        task.setIndex(request.getTo());

        if (request.getFromColumn() == request.getToColumn()) {
            if (request.getFrom() < request.getTo()) {
                columnFrom.stream()
                        .filter(t -> t.getIndex() >= request.getFrom() && t.getIndex() <= request.getTo()
                                && !Objects.equals(task.getId(), t.getId()))
                        .forEach(t -> t.setIndex(t.getIndex() - 1));
            } else {
                columnFrom.stream()
                        .filter(t -> t.getIndex() >= request.getTo() && t.getIndex() <= request.getFrom()
                                && !Objects.equals(task.getId(), t.getId()))
                        .forEach(t -> t.setIndex(t.getIndex() + 1));
            }
        } else {
            columnFrom.stream()
                    .filter(t -> t.getIndex() > request.getFrom())
                    .forEach(t -> t.setIndex(t.getIndex() - 1));

            taskRepo.changeColumnId(userId, projectId, request.getToColumn(), task.getId());

            columnTo.stream()
                    .filter(t -> t.getIndex() >= request.getTo())
                    .forEach(t -> t.setIndex(t.getIndex() + 1));
        }
    }
}
