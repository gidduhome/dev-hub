package com.taskflow.application.port.in;

import com.taskflow.domain.model.Project;

import java.util.List;

/**
 * Input port: all project-related use cases.
 */
public interface ProjectUseCase {

    // ---- Nested records (used as parameter / result types) --------------------

    record ProjectsPage(List<ProjectWithTaskCount> projects, long total) {}

    record ProjectWithTaskCount(Project project, long taskCount) {}

    record ProjectCreateCommand(String name, String description) {}

    record ProjectUpdateCommand(String name, String description) {}

    // ---- Use case methods -----------------------------------------------------

    ProjectsPage listProjects(int page, int size);

    ProjectWithTaskCount getProject(Long id);

    Project createProject(ProjectCreateCommand cmd);

    Project updateProject(Long id, ProjectUpdateCommand cmd);

    void deleteProject(Long id);
}
