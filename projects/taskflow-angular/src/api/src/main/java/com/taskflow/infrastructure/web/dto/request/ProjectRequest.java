package com.taskflow.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating a project.
 */
public record ProjectRequest(
        @NotBlank(message = "Project name must not be blank")
        @Size(max = 200, message = "Project name must not exceed 200 characters")
        String name,

        String description
) {}
