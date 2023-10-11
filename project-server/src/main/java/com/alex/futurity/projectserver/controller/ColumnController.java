package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.ProjectColumnDto;
import com.alex.futurity.projectserver.dto.RequestStringDto;
import com.alex.futurity.projectserver.service.ColumnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j

@RestController
@AllArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping("/{userId}/column/{projectId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public long createColumn(@PathVariable long userId, @PathVariable long projectId,
                             @Valid @RequestBody RequestStringDto columnName) {
        log.info("Handling creation column request. User id: {}, project id: {}, name: {}", userId, projectId, columnName.getValue());

        return columnService.createColumn(userId, projectId, columnName.getValue());
    }

    @GetMapping("/{userId}/column/{projectId}")
    public List<ProjectColumnDto> getColumns(@PathVariable long userId, @PathVariable long projectId) {
        log.info("Handling get columns request. User id: {}, project id: {}", userId, projectId);

        return columnService.getColumns(userId, projectId);
    }

    @DeleteMapping("/{userId}/column/{projectId}/{columnId}/delete/")
    @ResponseStatus(HttpStatus.OK)
    public void deleteColumn(@PathVariable long userId, @PathVariable long projectId, @PathVariable long columnId) {
        log.info("Handling deleting column request. User id: {}, project id: {}, column id: {}",
                userId, projectId, columnId);

        columnService.deleteColumn(userId, projectId, columnId);
    }

    @PatchMapping("/{userId}/column/{projectId}/index/change")
    @ResponseStatus(HttpStatus.OK)
    public void changeIndexColumn(@PathVariable long userId, @PathVariable long projectId,
                                  @RequestParam int from, @RequestParam int to) {
        log.info("Handling changing column index request. User id: {}, project id: {}, from {} to {}", userId, projectId,
                from, to);

        columnService.changeColumnIndex(userId, projectId, from, to);
    }

    @PatchMapping("/{userId}/column/{projectId}/{columnId}/name/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changeColumnName(@PathVariable long userId, @PathVariable long projectId, @PathVariable long columnId,
                                 @Valid @RequestBody RequestStringDto columnName) {
        log.info("Handling changing column name request. User id: {}, project id: {}, column id: {}, columnName: {}",
                userId, projectId, columnId, columnName.getValue());

        columnService.changeColumnName(userId, projectId, columnId, columnName.getValue());
    }

    @PatchMapping("/{userId}/column/{projectId}/mark")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void markColumnAsDone(@PathVariable long userId, @PathVariable long projectId,
                                 @RequestParam(value = "columnIdToUnmark", required = false) Long columnToUnmark,
                                 @RequestParam(value = "columnIdToMark") long columnToMark) {
        log.info("Handlong marking column request. User id: {}, project id: {}, column to mark: {}, column to unmark: {}",
                userId, projectId, columnToMark, columnToUnmark);

        columnService.markColumnAsDone(userId, projectId, columnToMark, columnToUnmark);
    }
}
