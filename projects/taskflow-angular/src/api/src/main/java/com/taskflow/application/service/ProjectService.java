package com.taskflow.application.service;

import com.taskflow.application.exception.ResourceNotFoundException;
import com.taskflow.application.port.in.ProjectUseCase;
import com.taskflow.application.port.out.ProjectOutputPort;
import com.taskflow.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service: implements all project use cases.
 * Only depends on ports — no JPA, no HTTP.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase {

    private final ProjectOutputPort projectOutputPort;

    // ---- Query ---------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public ProjectsPage listProjects(int page, int size) {
        ProjectOutputPort.ProjectsPage result =
                projectOutputPort.findAllWithTaskCount(page, size);

        List<ProjectWithTaskCount> mapped = result.items().stream()
                .map(item -> new ProjectWithTaskCount(item.project(), item.taskCount()))
                .toList();

        return new ProjectsPage(mapped, result.total());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectWithTaskCount getProject(Long id) {
        return projectOutputPort.findByIdWithTaskCount(id)
                .map(item -> new ProjectWithTaskCount(item.project(), item.taskCount()))
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    // ---- Command -------------------------------------------------------------

    @Override
    public Project createProject(ProjectCreateCommand cmd) {
        Project project = Project.create(cmd.name(), cmd.description());
        return projectOutputPort.save(project);
    }

    @Override
    public Project updateProject(Long id, ProjectUpdateCommand cmd) {
        Project existing = projectOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));

        Project updated = existing;
        if (cmd.name() != null && !cmd.name().isBlank()) {
            updated = updated.withName(cmd.name());
        }
        if (cmd.description() != null) {
            updated = updated.withDescription(cmd.description());
        }

        return projectOutputPort.save(updated);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectOutputPort.existsById(id)) {
            throw new ResourceNotFoundException("Project", id);
        }
        projectOutputPort.deleteById(id);
    }
}
