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
        ProjectColumn column = new ProjectColumn(request.getName());
        Project project = projectService.findByIdAndUserId(request.getProjectId(), request.getUserId());
        columnService.saveColumn(column);
        column.setProject(project);

        return new IdResponse(column.getId());
    }

    @Override
    @Transactional
    public ListResponse<ColumnDTO> getColumns(TwoIdRequestDTO request) {
        long userId = request.getFirstId();
        long projectId = request.getSecondId();
        Project project = projectService.findByIdAndUserId(userId, projectId);
        List<ProjectColumn> columns = project.getColumns();
        List<ColumnDTO> dtos = columns.stream().map(ColumnDTO::new).collect(Collectors.toList());

        return new ListResponse<>(dtos);
    }

    @Override
    @Transactional
    public void deleteColumn(ThreeIdRequestDTO request) {
        Project project = projectService.findByIdAndUserId(request.getSecondId(), request.getFirstId());
        ProjectColumn column = columnService.findColumnById(request.getThirdId());


        if (project.getId().equals(column.getProject().getId())) {
            columnService.deleteColumn(column);
        } else {
            throw new ClientSideException("A column is associated with such data does not exist", HttpStatus.NOT_FOUND);
        }
    }
}
