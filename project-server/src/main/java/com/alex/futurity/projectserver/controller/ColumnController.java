package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.ProjectColumnDto;
import com.alex.futurity.projectserver.service.ColumnService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Log4j2
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping("/{userId}/column/{projectId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public long createColumn(@PathVariable long userId, @PathVariable long projectId,
                                                   @RequestParam String columnName) {
        log.info("Handling creation column request. User id: {}, project id: {}, name: {}", userId, projectId, columnName);

        return columnService.createColumn(userId, projectId, columnName);
    }

    @GetMapping("/{userId}/column/{projectId}")
    public List<ProjectColumnDto> getColumns(@PathVariable long userId, @PathVariable long projectId) {
        log.info("Handling get columns request. User id: {}, project id: {}", userId, projectId);

        return columnService.getColumns(userId, projectId);
    }

    @DeleteMapping("/{userId}/column/{projectId}/delete/{columnIndex}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteColumn(@PathVariable long userId, @PathVariable long projectId, @PathVariable int columnIndex) {
        log.info("Handling deleting column request. User id: {}, project id: {}, column id: {}",
                userId, projectId, columnIndex);

        columnService.deleteColumn(userId, projectId, columnIndex);
    }

    @PatchMapping("/{userId}/column/{projectId}/index/change")
    @ResponseStatus(HttpStatus.OK)
    public void changeIndexColumn(@PathVariable long userId, @PathVariable long projectId,
                                  @RequestParam int from, @RequestParam int to) {
        log.info("Handling changing column index request. User id: {}, project id: {}, from {} to {}", userId, projectId,
                from, to);

        columnService.changeColumnIndex(userId, projectId, from, to);
    }
}
