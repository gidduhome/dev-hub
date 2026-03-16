package com.taskflow.infrastructure.persistence.repository;

import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;
import com.taskflow.infrastructure.persistence.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for {@link TaskEntity}.
 */
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * Filtered, paginated task query.
     *
     * <p>Each filter parameter is optional: when {@code null} is passed the
     * corresponding predicate evaluates to {@code true} and is effectively skipped.
     * Hibernate correctly handles {@code null} bind parameters in JPQL {@code IS NULL OR …}
     * expressions.</p>
     */
    @Query("""
            SELECT t FROM TaskEntity t
            WHERE  (:projectId IS NULL OR t.projectId = :projectId)
            AND    (:status    IS NULL OR t.status    = :status)
            AND    (:priority  IS NULL OR t.priority  = :priority)
            AND    (:search    IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')))
            ORDER BY t.createdAt DESC
            """)
    Page<TaskEntity> findAllWithFilters(
            @Param("projectId") Long projectId,
            @Param("status")    TaskStatus status,
            @Param("priority")  TaskPriority priority,
            @Param("search")    String search,
            Pageable pageable);
}
