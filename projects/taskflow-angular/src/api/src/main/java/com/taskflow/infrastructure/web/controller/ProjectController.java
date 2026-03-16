package com.taskflow.infrastructure.web.controller;

import com.taskflow.application.port.in.ProjectUseCase;
import com.taskflow.domain.model.Project;
import com.taskflow.infrastructure.web.dto.request.ProjectRequest;
import com.taskflow.infrastructure.web.dto.request.ProjectUpdateRequest;
import com.taskflow.infrastructure.web.dto.response.ApiResponse;
import com.taskflow.infrastructure.web.dto.response.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for project resources.
 */
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
public class ProjectController {

    private final ProjectUseCase projectUseCase;

    @GetMapping
    @Operation(summary = "List all projects with task counts")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ProjectUseCase.ProjectsPage result = projectUseCase.listProjects(page, size);

        List<ProjectResponse> responses = result.projects().stream()
                .map(ProjectResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.paginated(responses, page, size, result.total()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single project by ID")
    public ResponseEntity<ApiResponse<ProjectResponse>> get(@PathVariable Long id) {
        ProjectUseCase.ProjectWithTaskCount pwc = projectUseCase.getProject(id);
        return ResponseEntity.ok(ApiResponse.ok(ProjectResponse.from(pwc)));
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ApiResponse<ProjectResponse>> create(
            @Valid @RequestBody ProjectRequest req) {

        Project created = projectUseCase.createProject(
                new ProjectUseCase.ProjectCreateCommand(req.name(), req.description()));

        // Re-fetch to include task count (0 for brand-new project)
        ProjectUseCase.ProjectWithTaskCount pwc = projectUseCase.getProject(created.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(ProjectResponse.from(pwc)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a project")
    public ResponseEntity<ApiResponse<ProjectResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateRequest req) {

        projectUseCase.updateProject(id,
                new ProjectUseCase.ProjectUpdateCommand(req.name(), req.description()));

        ProjectUseCase.ProjectWithTaskCount pwc = projectUseCase.getProject(id);
        return ResponseEntity.ok(ApiResponse.ok(ProjectResponse.from(pwc)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project and all its tasks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectUseCase.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
