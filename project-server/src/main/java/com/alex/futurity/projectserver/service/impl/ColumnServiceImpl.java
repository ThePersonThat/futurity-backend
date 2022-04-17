package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.ProjectColumnDto;
import com.alex.futurity.projectserver.entity.ProjectColumn;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.service.ColumnService;
import com.alex.futurity.projectserver.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ColumnServiceImpl implements ColumnService {
    private ProjectService projectService;

    @Override
    public long createColumn(long userId, long projectId, String columnName) {
        ProjectColumn projectColumn = projectService.addColumnToProject(userId, projectId, columnName);

        return projectColumn.getId();
    }

    @Override
    @Transactional
    public List<ProjectColumnDto> getColumns(long userId, long projectId) {
        List<ProjectColumn> columns = projectService.getColumnsFromProject(userId, projectId);

        return columns.stream()
                .map(ProjectColumnDto::fromProjectColumn)
                .sorted(Comparator.comparingInt(ProjectColumnDto::getIndex))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteColumn(long userId, long projectId, int columnIndex) {
        List<ProjectColumn> columns = projectService.getColumnsFromProject(userId, projectId);

        if (columnIndex < 0 || columnIndex > columns.size()) {
            throw new ClientSideException("Columns out of bounds", HttpStatus.BAD_REQUEST);
        }

        columns.stream()
                .filter(column -> column.getIndex() > columnIndex)
                .forEach(column -> column.setIndex(column.getIndex() - 1));
        columns.remove(columnIndex);
    }

    @Override
    @Transactional
    public void changeColumnIndex(long userId, long projectId, int from, int to) {
        List<ProjectColumn> columns = projectService.getColumnsFromProject(userId, projectId)
                .stream().sorted(Comparator.comparingInt(ProjectColumn::getIndex))
                .collect(Collectors.toList());

        if (from < 0 || from > columns.size() || to < 0 || to > columns.size() + 1 || from == to) {
            throw new ClientSideException("Columns out of bounds", HttpStatus.BAD_REQUEST);
        }

        ProjectColumn columnFrom = columns.get(from);
        columnFrom.setIndex(to);

        if (from < to) {
            columns.stream()
                    .filter(column -> column.getIndex() >= from && column.getIndex() <= to
                            && !Objects.equals(columnFrom.getId(), column.getId()))
                    .forEach(column -> column.setIndex(column.getIndex() - 1));
        } else {
            columns.stream()
                    .filter(column -> column.getIndex() >= to && column.getIndex() <= from
                            && !Objects.equals(columnFrom.getId(), column.getId()))
                    .forEach(column -> column.setIndex(column.getIndex() + 1));
        }
    }
}
