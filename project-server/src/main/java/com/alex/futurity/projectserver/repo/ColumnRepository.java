package com.alex.futurity.projectserver.repo;

import com.alex.futurity.projectserver.entity.ProjectColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<ProjectColumn, Long> {
}
