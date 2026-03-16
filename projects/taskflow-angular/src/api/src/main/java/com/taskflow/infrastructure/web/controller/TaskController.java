package com.taskflow.infrastructure.web.controller;

import com.taskflow.application.port.in.TaskUseCase;
import com.taskflow.domain.model.Task;
import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;
import com.taskflow.infrastructure.web.dto.request.TaskRequest;
import com.taskflow.infrastructure.web.dto.request.TaskUpdateRequest;
import com.taskflow.infrastructure.web.dto.response.ApiResponse;
import com.taskflow.infrastructure.web.dto.response.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for task resources.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskUseCase taskUseCase;

    @GetMapping
    @Operation(summary = "List tasks with optional filters")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> list(
            @RequestParam(defaultValue = "0")  int          page,
            @RequestParam(defaultValue = "20") int          size,
            @RequestParam(required = false)    Long         projectId,
            @RequestParam(required = false)    TaskStatus   status,
            @RequestParam(required = false)    TaskPriority priority,
            @RequestParam(required = false)    String       search) {

        TaskUseCase.TaskFilter filter = new TaskUseCase.TaskFilter(projectId, status, priority, search);
        TaskUseCase.TasksPage result  = taskUseCase.listTasks(page, size, filter);

        List<TaskResponse> responses = result.tasks().stream()
                .map(TaskResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.paginated(responses, page, size, result.total()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single task by ID")
    public ResponseEntity<ApiResponse<TaskResponse>> get(@PathVariable Long id) {
        Task task = taskUseCase.getTask(id);
        return ResponseEntity.ok(ApiResponse.ok(TaskResponse.from(task)));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<ApiResponse<TaskResponse>> create(
            @Valid @RequestBody TaskRequest req) {

        Task created = taskUseCase.createTask(new TaskUseCase.TaskCreateCommand(
                req.title(),
                req.description(),
                req.status(),
                req.priority(),
                req.projectId()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(TaskResponse.from(created)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a task")
    public ResponseEntity<ApiResponse<TaskResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest req) {

        Task updated = taskUseCase.updateTask(id, new TaskUseCase.TaskUpdateCommand(
                req.title(),
                req.description(),
                req.status(),
                req.priority(),
                req.projectId()));

        return ResponseEntity.ok(ApiResponse.ok(TaskResponse.from(updated)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskUseCase.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
