package com.taskflow.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Pure domain model for a Task.
 * No Spring, no JPA — only plain Java.
 */
public final class Task {

    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final TaskPriority priority;
    private final Long projectId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Task(Long id, String title, String description,
                 TaskStatus status, TaskPriority priority, Long projectId,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        Objects.requireNonNull(title, "Task title must not be null");
        Objects.requireNonNull(projectId, "Task projectId must not be null");
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.status      = status != null ? status : TaskStatus.TODO;
        this.priority    = priority != null ? priority : TaskPriority.MEDIUM;
        this.projectId   = projectId;
        this.createdAt   = createdAt;
        this.updatedAt   = updatedAt;
    }

    // ---- Static factory -------------------------------------------------------

    /**
     * Create a new (unsaved) Task.  id and timestamps are left null.
     */
    public static Task create(String title, String description,
                              TaskStatus status, TaskPriority priority,
                              Long projectId) {
        return new Task(null, title, description, status, priority, projectId, null, null);
    }

    /**
     * Reconstitute a Task from persistent storage.
     */
    public static Task reconstitute(Long id, String title, String description,
                                    TaskStatus status, TaskPriority priority,
                                    Long projectId,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    // ---- With-methods ---------------------------------------------------------

    public Task withTitle(String title) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withDescription(String description) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withStatus(TaskStatus status) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withPriority(TaskPriority priority) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withProjectId(Long projectId) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withUpdatedAt(LocalDateTime updatedAt) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withId(Long id) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    public Task withTimestamps(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Task(id, title, description, status, priority, projectId, createdAt, updatedAt);
    }

    // ---- Accessors ------------------------------------------------------------

    public Long getId()                  { return id; }
    public String getTitle()             { return title; }
    public String getDescription()       { return description; }
    public TaskStatus getStatus()        { return status; }
    public TaskPriority getPriority()    { return priority; }
    public Long getProjectId()           { return projectId; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    // ---- equals / hashCode / toString -----------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status=" + status + "}";
    }
}
