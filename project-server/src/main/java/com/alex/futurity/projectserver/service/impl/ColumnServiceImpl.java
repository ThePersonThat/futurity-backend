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
}
