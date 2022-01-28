package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.entity.ProjectColumn;

public interface ColumnService {
    void saveColumn(ProjectColumn column);
    ProjectColumn findColumnById(long id);
    void deleteColumn(ProjectColumn column);
    void shiftColumnsIndex(int index, long projectId);
    void changeColumnIndex(int from, int to, long projectId);
}
