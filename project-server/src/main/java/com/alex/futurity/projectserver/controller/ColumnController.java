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

    @PostMapping("/{id}/column/{projectId}/create")
    public ResponseEntity<IdResponse> createColumn(@PathVariable long id, @PathVariable long projectId,
                                                   @Valid @RequestBody CreationColumnRequestDTO request) {
        request.setUserId(id);
        request.setProjectId(projectId);

        return new ResponseEntity<>(columnService.createColumn(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/column/{projectId}")
    public ResponseEntity<ListResponse<ColumnDTO>> getColumns(@PathVariable long id, @PathVariable long projectId) {
        TwoIdRequestDTO request = new TwoIdRequestDTO(id, projectId);

        return ResponseEntity.ok(columnService.getColumns(request));
    }

    @DeleteMapping("/{id}/column/{projectId}/delete/{columnId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteColumn(@PathVariable long id, @PathVariable long projectId, @PathVariable long columnId) {
        ThreeIdRequestDTO request = new ThreeIdRequestDTO(id, projectId, columnId);

        columnService.deleteColumn(request);
    }
}
