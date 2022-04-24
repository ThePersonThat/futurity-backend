package com.alex.futurity.projectserver.repo;

import com.alex.futurity.projectserver.entity.ProjectColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<ProjectColumn, Long> {
    Optional<ProjectColumn> findProjectColumnByIndexAndProjectUserIdAndProjectId(int columnIndex, long userId, long longProjectId);
    List<ProjectColumn> findAllByProjectIdAndProjectUserIdOrderByIndex(long projectId, long userId);
}
