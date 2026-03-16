package com.taskflow.infrastructure.web.dto.response;

import com.taskflow.application.port.in.ProjectUseCase;

import java.time.LocalDateTime;

/**
 * API response DTO for a project.
 */
public record ProjectResponse(
        Long          id,
        String        name,
        String        description,
        long          taskCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProjectResponse from(ProjectUseCase.ProjectWithTaskCount pwc) {
        return new ProjectResponse(
                pwc.project().getId(),
                pwc.project().getName(),
                pwc.project().getDescription(),
                pwc.taskCount(),
                pwc.project().getCreatedAt(),
                pwc.project().getUpdatedAt()
        );
    }
}
