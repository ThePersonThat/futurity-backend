package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.entity.ProjectColumn;

public interface ColumnService {
    void saveColumn(ProjectColumn column);
    ProjectColumn findColumnById(long id);
    ProjectColumn findColumnByProjectColumnIndexAndProjectId(long columnIndex, long projectId);
    void deleteColumn(ProjectColumn column);
    void shiftColumnsIndex(int index, long projectId);
    void changeColumnIndex(int from, int to, long projectId);
    ProjectColumn findColumn(long columnId, long projectId, long userId);
}
