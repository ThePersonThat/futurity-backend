package com.alex.futurity.projectserver.repo;

import com.alex.futurity.projectserver.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByNameAndUserId(String name, long id);
    List<Project> findAllByUserId(long id);
    Optional<Project> findByIdAndUserId(long id, long userId);

    @Modifying
    @Query("delete from Project where id = ?1 and userId = ?2")
    int deleteProjectByIdAndUserId(long id, long userId);
}
