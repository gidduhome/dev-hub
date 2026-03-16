package com.taskflow.infrastructure.web.dto.response;

import com.taskflow.domain.model.Task;
import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;

import java.time.LocalDateTime;

/**
 * API response DTO for a task.
 */
public record TaskResponse(
        Long          id,
        String        title,
        String        description,
        TaskStatus    status,
        TaskPriority  priority,
        Long          projectId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProjectId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
