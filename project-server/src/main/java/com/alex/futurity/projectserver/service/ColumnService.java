package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.ProjectColumnDto;

import java.util.List;

public interface ColumnService {
    long createColumn(long userId, long porjectId, String columnName);
    List<ProjectColumnDto> getColumns(long userId, long projectId);
    void deleteColumn(long userId, long projectId, int columnIndex);
    void changeColumnIndex(long userId, long projectId, int from, int to);
}
