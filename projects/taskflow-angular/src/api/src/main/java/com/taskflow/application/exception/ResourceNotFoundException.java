package com.taskflow.application.exception;

/**
 * Thrown when a requested resource cannot be found in the system.
 * Maps to HTTP 404 in the global exception handler.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final Long   id;

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
        this.resource = resource;
        this.id       = id;
    }

    public String getResource() { return resource; }
    public Long   getId()       { return id; }
}
