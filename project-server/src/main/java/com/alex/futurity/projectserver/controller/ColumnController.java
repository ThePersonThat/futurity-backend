package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.dto.*;
import com.alex.futurity.projectserver.service.ColumnManagerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
public class ColumnController {
    private final ColumnManagerService columnService;

    @PostMapping("/{userId}/column/{projectId}/create")
    public ResponseEntity<IdResponse> createColumn(@PathVariable long userId, @PathVariable long projectId,
                                                   @Valid @RequestBody CreationColumnRequestDTO request) {
        request.setUserId(userId);
        request.setProjectId(projectId);

        return new ResponseEntity<>(columnService.createColumn(request), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/column/{projectId}")
    public ResponseEntity<ListResponse<ColumnDTO>> getColumns(@PathVariable long userId, @PathVariable long projectId) {
        TwoIdRequestDTO request = new TwoIdRequestDTO(userId, projectId);

        return ResponseEntity.ok(columnService.getColumns(request));
    }

    @DeleteMapping("/{userId}/column/{projectId}/delete/{columnId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteColumn(@PathVariable long userId, @PathVariable long projectId, @PathVariable long columnId) {
        ThreeIdRequestDTO request = new ThreeIdRequestDTO(userId, projectId, columnId);

        columnService.deleteColumn(request);
    }

    @PatchMapping("/{userId}/column/{projectId}/index/change")
    public void changeIndexColumn(@PathVariable long userId, @PathVariable long projectId,
                                  @Valid @RequestBody ChangeIndexColumnRequestDTO request) {
        request.setUserId(userId);
        request.setProjectId(projectId);

        columnService.changeIndexColumn(request);
    }
}
