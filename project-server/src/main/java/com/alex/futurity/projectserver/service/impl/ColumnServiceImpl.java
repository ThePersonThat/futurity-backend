package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.entity.ProjectColumn;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.repo.ColumnRepository;
import com.alex.futurity.projectserver.service.ColumnService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ColumnServiceImpl implements ColumnService {
    private final ColumnRepository columnRepo;

    @Override
    public void saveColumn(ProjectColumn column) {
        columnRepo.save(column);
    }

    @Override
    public ProjectColumn findColumnById(long id) {
        return columnRepo.findById(id).orElseThrow(() -> {
            throw new ClientSideException("Column with " + id + " is not found", HttpStatus.NOT_FOUND);
        });
    }

    @Override
    public void deleteColumn(ProjectColumn column) {
        columnRepo.delete(column);
    }

    @Override
    public void shiftColumnsIndex(int startIndex, long projectId) {
        columnRepo.shiftColumnsIndex(startIndex, projectId);
    }

    @Override
    public void changeColumnIndex(int from, int to, long projectId) {
        ProjectColumn column = columnRepo.getProjectColumnByIndexAndProjectId(from, projectId)
                .orElseThrow(() -> new ClientSideException("Column with index is not found", HttpStatus.NOT_FOUND));
        column.setIndex(to);

        if (to > from) {
            columnRepo.shiftBack(from, to, column.getId(), projectId);
        } else {
            columnRepo.shiftForward(from, to, column.getId(), projectId);
        }
    }

    @Override
    public ProjectColumn findColumn(long columnId, long projectId, long userId) {
        return columnRepo.findByIdAndProjectIdAndProjectUserId(columnId, projectId, userId).orElseThrow(() -> {
                    throw new ClientSideException("The task is associated with such data does not exist", HttpStatus.NOT_FOUND);
        });
    }
}
