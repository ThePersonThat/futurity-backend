package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.*;
import com.alex.futurity.projectserver.entity.Project;
import com.alex.futurity.projectserver.entity.ProjectColumn;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.service.ColumnManagerService;
import com.alex.futurity.projectserver.service.ColumnService;
import com.alex.futurity.projectserver.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ColumnManagerServiceImpl implements ColumnManagerService {
    private final ColumnService columnService;
    private final ProjectService projectService;

    @Override
    @Transactional
    public IdResponse createColumn(CreationColumnRequestDTO request) {
        Project project = projectService.findByIdAndUserId(request.getProjectId(), request.getUserId());
        ProjectColumn column = new ProjectColumn(request.getName(), project.getLastColumnIndex());
        columnService.saveColumn(column);
        column.setProject(project);

        return new IdResponse(column.getId());
    }

    @Override
    @Transactional
    public ListResponse<ColumnDTO> getColumns(TwoIdRequestDTO request) {
        long userId = request.getFirstId();
        long projectId = request.getSecondId();
        Project project = projectService.findByIdAndUserId(projectId, userId);
        List<ProjectColumn> columns = project.getColumns();
        List<ColumnDTO> dtos = columns.stream()
                .map(ColumnDTO::new)
                .sorted(Comparator.comparingInt(ColumnDTO::getIndex))
                .collect(Collectors.toList());

        return new ListResponse<>(dtos);
    }

    @Override
    @Transactional
    public void deleteColumn(ThreeIdRequestDTO request) {
        long projectId = request.getSecondId();
        Project project = projectService.findByIdAndUserId(projectId, request.getFirstId());
        ProjectColumn column = columnService.findColumnByProjectColumnIndexAndProjectId(projectId, request.getThirdId());

        if (project.getId().equals(column.getProject().getId())) {
            int index = column.getIndex();
            columnService.deleteColumn(column);
            columnService.shiftColumnsIndex(index, project.getId());
        } else {
            throw new ClientSideException("A column is associated with such data does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void changeIndexColumn(ChangeIndexColumnRequestDTO request) {
        Project project = projectService.findByIdAndUserId(request.getProjectId(), request.getUserId());

        if (project.getColumns().size() + 1 < request.getTo()) {
            throw new ClientSideException("Columns out of bounds", HttpStatus.BAD_REQUEST);
        }
        columnService.changeColumnIndex(request.getFrom(), request.getTo(), project.getId());
    }
}
