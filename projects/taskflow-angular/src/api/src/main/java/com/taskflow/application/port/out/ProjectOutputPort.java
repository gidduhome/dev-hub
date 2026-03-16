package com.taskflow.application.port.out;

import com.taskflow.domain.model.Project;

import java.util.List;
import java.util.Optional;

/**
 * Output port: persistence operations for projects.
 */
public interface ProjectOutputPort {

    record ProjectWithCount(Project project, long taskCount) {}

    record ProjectsPage(List<ProjectWithCount> items, long total) {}

    ProjectsPage findAllWithTaskCount(int page, int size);

    Optional<Project> findById(Long id);

    Optional<ProjectWithCount> findByIdWithTaskCount(Long id);

    Project save(Project project);

    void deleteById(Long id);

    boolean existsById(Long id);
}
