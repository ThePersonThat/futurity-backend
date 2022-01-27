package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.entity.ProjectColumn;

public interface ColumnService {
    void saveColumn(ProjectColumn column);
    ProjectColumn findColumnById(long id);
    void deleteColumn(ProjectColumn column);
}
