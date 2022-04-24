package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.ChangeTaskIndexRequestDto;
import com.alex.futurity.projectserver.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Log4j2
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/{userId}/task/{projectId}/{columnIndex}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public long createTask(@PathVariable long userId, @PathVariable long projectId, @PathVariable int columnIndex,
                           @RequestParam String taskName) {
        log.info("Handling creation task request. User id: {}, project id: {}, column index: {}, name: {}",
                userId, projectId, columnIndex, taskName);

        return taskService.createTask(userId, projectId, columnIndex, taskName);
    }

    @DeleteMapping("/{userId}/task/{projectId}/{columnIndex}/{taskIndex}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable long userId, @PathVariable long projectId, @PathVariable int columnIndex,
                           @PathVariable int taskIndex) {
        log.info("Handling deleting task request. User id: {}, project id: {}, column index: {}, taskIndex: {}",
                userId, projectId, columnIndex, taskIndex);

        taskService.deleteTask(userId, projectId, columnIndex, taskIndex);
    }

    @PatchMapping("/{userId}/task/{projectId}/index/change")
    @ResponseStatus(HttpStatus.OK)
    public void changeTaskIndex(@PathVariable long userId, @PathVariable long projectId,
                                @RequestBody ChangeTaskIndexRequestDto request) {
        log.info("Handling changing task index request. User id: {}, project id: {}, Data: {}",
                projectId, userId, request);

        taskService.changeTaskIndex(userId, projectId, request);
    }
}
