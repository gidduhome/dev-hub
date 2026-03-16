package com.taskflow.infrastructure.web.dto.response;

import java.util.Map;

/**
 * Generic envelope for all API responses.
 *
 * @param <T> the payload type
 */
public record ApiResponse<T>(T data, String error, Object meta) {

    /** Successful response with a payload and empty metadata. */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, null, Map.of());
    }

    /** Successful paginated response — metadata carries pagination info. */
    public static <T> ApiResponse<T> paginated(T data, int page, int size, long total) {
        return new ApiResponse<>(data, null,
                Map.of("page", page, "size", size, "total", total));
    }

    /** Error response — data is null, error carries the human-readable message. */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, message, Map.of());
    }
}
