package com.taskflow.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Pure domain model for a Project.
 * No Spring, no JPA — only plain Java.
 */
public final class Project {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Project(Long id, String name, String description,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        Objects.requireNonNull(name, "Project name must not be null");
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.createdAt   = createdAt;
        this.updatedAt   = updatedAt;
    }

    // ---- Static factory -------------------------------------------------------

    /**
     * Create a new (unsaved) Project.  id and timestamps are left null;
     * the persistence layer fills them in on first save.
     */
    public static Project create(String name, String description) {
        return new Project(null, name, description, null, null);
    }

    /**
     * Reconstitute a Project from persistent storage (all fields known).
     */
    public static Project reconstitute(Long id, String name, String description,
                                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Project(id, name, description, createdAt, updatedAt);
    }

    // ---- With-methods (returns new instances — immutable style) ---------------

    public Project withName(String name) {
        return new Project(this.id, name, this.description, this.createdAt, this.updatedAt);
    }

    public Project withDescription(String description) {
        return new Project(this.id, this.name, description, this.createdAt, this.updatedAt);
    }

    public Project withUpdatedAt(LocalDateTime updatedAt) {
        return new Project(this.id, this.name, this.description, this.createdAt, updatedAt);
    }

    public Project withId(Long id) {
        return new Project(id, this.name, this.description, this.createdAt, this.updatedAt);
    }

    public Project withTimestamps(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Project(this.id, this.name, this.description, createdAt, updatedAt);
    }

    // ---- Accessors ------------------------------------------------------------

    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    // ---- equals / hashCode / toString -----------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{id=" + id + ", name='" + name + "'}";
    }
}
