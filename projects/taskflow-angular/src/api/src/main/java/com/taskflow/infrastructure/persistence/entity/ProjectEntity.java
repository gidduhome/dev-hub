package com.taskflow.infrastructure.persistence.entity;

import com.taskflow.domain.model.Project;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity for the {@code projects} table.
 * Conversion to/from the domain model is handled via {@link #toDomain()} and {@link #fromDomain(Project)}.
 */
@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ---- Mapping --------------------------------------------------------------

    public Project toDomain() {
        return Project.reconstitute(id, name, description, createdAt, updatedAt);
    }

    public static ProjectEntity fromDomain(Project p) {
        return ProjectEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
