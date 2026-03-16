package com.taskflow.infrastructure.persistence.entity;

import com.taskflow.domain.model.Task;
import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity for the {@code tasks} table.
 */
@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Stored as the PostgreSQL enum {@code task_status}.
     * We use {@code columnDefinition} so Hibernate treats it as a named type rather
     * than a plain VARCHAR, which keeps the DB schema consistent with Flyway.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "task_status")
    private TaskStatus status;

    /**
     * Stored as the PostgreSQL enum {@code task_priority}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "task_priority")
    private TaskPriority priority;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ---- Mapping --------------------------------------------------------------

    public Task toDomain() {
        return Task.reconstitute(id, title, description, status, priority,
                projectId, createdAt, updatedAt);
    }

    public static TaskEntity fromDomain(Task t) {
        return TaskEntity.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .priority(t.getPriority())
                .projectId(t.getProjectId())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
