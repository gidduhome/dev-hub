package com.taskflow.infrastructure.web.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Request body for partially updating a project (PATCH semantics).
 * All fields are optional — only non-null values are applied.
 */
public record ProjectUpdateRequest(
        @Size(max = 200, message = "Project name must not exceed 200 characters")
        String name,

        String description
) {}
