package com.taskflow.infrastructure.web.dto.request;

import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;
import jakarta.validation.constraints.Size;

/**
 * Request body for partially updating a task (PATCH semantics).
 * All fields are optional — only non-null values are applied.
 */
public record TaskUpdateRequest(
        @Size(max = 300, message = "Task title must not exceed 300 characters")
        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        Long projectId
) {}
