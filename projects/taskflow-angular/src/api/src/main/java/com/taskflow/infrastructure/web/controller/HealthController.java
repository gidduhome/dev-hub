package com.taskflow.infrastructure.web.controller;

import com.taskflow.infrastructure.web.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Lightweight health-check endpoint for load-balancers and readiness probes.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Application health check")
public class HealthController {

    private static final String VERSION = "0.1.0";

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.ok(Map.of("status", "healthy", "version", VERSION));
    }
}
