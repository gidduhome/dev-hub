package com.taskflow.infrastructure.web.dto.request;

import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating a task.
 */
public record TaskRequest(
        @NotBlank(message = "Task title must not be blank")
        @Size(max = 300, message = "Task title must not exceed 300 characters")
        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        @NotNull(message = "projectId is required")
        Long projectId
) {}
