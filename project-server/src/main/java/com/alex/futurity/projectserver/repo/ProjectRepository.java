package com.alex.futurity.projectserver.repo;

import com.alex.futurity.projectserver.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByNameAndUserId(String name, long id);
    List<Project> findAllByUserId(long id);
    Optional<Project> findByIdAndUserId(long id, long userId);
}
