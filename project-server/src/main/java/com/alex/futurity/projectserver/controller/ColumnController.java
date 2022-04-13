package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.*;
import com.alex.futurity.projectserver.service.ColumnManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
@Log4j2
public class ColumnController {
    private final ColumnManagerService columnService;

    @PostMapping("/{userId}/column/{projectId}/create")
    public ResponseEntity<IdResponse> createColumn(@PathVariable long userId, @PathVariable long projectId,
                                                   @Valid @RequestBody CreationColumnRequestDTO request) {
        log.info("Handling new create column request. User id: {}, project id: {}, dto: {}", userId, projectId, request);
        request.setUserId(userId);
        request.setProjectId(projectId);

        return new ResponseEntity<>(columnService.createColumn(request), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/column/{projectId}")
    public ResponseEntity<ListResponse<ColumnDTO>> getColumns(@PathVariable long userId, @PathVariable long projectId) {
        log.info("Handling get columns request. User id: {}, project id: {}", userId, projectId);
        TwoIdRequestDTO request = new TwoIdRequestDTO(userId, projectId);

        return ResponseEntity.ok(columnService.getColumns(request));
    }

    @DeleteMapping("/{userId}/column/{projectId}/delete/{columnId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteColumn(@PathVariable long userId, @PathVariable long projectId, @PathVariable long columnId) {
        log.info("Handling delete column request. User id: {}, project id: {}, column id: {}", userId, projectId, columnId);
        ThreeIdRequestDTO request = new ThreeIdRequestDTO(userId, projectId, columnId);

        columnService.deleteColumn(request);
    }

    @PatchMapping("/{userId}/column/{projectId}/index/change")
    public void changeIndexColumn(@PathVariable long userId, @PathVariable long projectId,
                                  @Valid @RequestBody ChangeIndexColumnRequestDTO request) {
        log.info("Handling change  column index request. User id: {}, project id: {}, dto: {}", userId, projectId, request);

        request.setUserId(userId);
        request.setProjectId(projectId);

        columnService.changeIndexColumn(request);
    }
}
