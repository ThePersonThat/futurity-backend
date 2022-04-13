package com.alex.futurity.projectserver.repo;

import com.alex.futurity.projectserver.entity.ProjectColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<ProjectColumn, Long> {
    @Modifying
    @Query("UPDATE ProjectColumn p SET p.index = p.index - 1 WHERE p.index > ?1 AND p.project.id = ?2")
    void shiftColumnsIndex(int startIndex, long projectId);

    @Query("SELECT p FROM ProjectColumn p WHERE p.index = ?1 AND p.project.id = ?2")
    Optional<ProjectColumn> getProjectColumnByIndexAndProjectId(int index, long projectId);

    @Modifying
    @Query("UPDATE ProjectColumn p SET p.index = p.index - 1 WHERE p.index <= ?2 AND p.index >= ?1 " +
            "AND p.id <> ?3 AND p.project.id = ?4")
    void shiftBack(int from, int to, long exceptId, long projectId);

    @Modifying
    @Query("UPDATE ProjectColumn p SET p.index = p.index + 1 WHERE p.index <= ?1 AND p.index >= ?2 " +
            "AND p.id <> ?3 AND p.project.id = ?4")
    void shiftForward(int from, int to, long exceptId, long projectId);

    Optional<ProjectColumn> findByIdAndProjectIdAndProjectUserId(long columnId, long projectId, long userId);

    Optional<ProjectColumn> findProjectColumnByIndexAndProjectId(int columnId, long projectId);

    void deleteProjectColumnByIndexAndProjectId(int columnIndex, long projectId);
}
