package com.taskflow.infrastructure.persistence.repository;

import com.taskflow.infrastructure.persistence.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for {@link ProjectEntity}.
 *
 * <p>The task-count queries use a native SQL sub-select approach to avoid
 * the limitations of JPQL {@code Object[]} + {@code COUNT} across unrelated entities.
 * Returning a {@code Long[]} array keeps the mapping simple in the adapter.</p>
 */
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {

    /**
     * Returns a page of rows where each row is {@code [project_id, task_count]}.
     * The adapter fetches each {@link ProjectEntity} by id and pairs it with the count.
     */
    @Query(value = """
            SELECT p.id                                    AS project_id,
                   COUNT(t.id)                             AS task_count
            FROM   projects p
            LEFT JOIN tasks t ON t.project_id = p.id
            GROUP BY p.id
            ORDER BY p.created_at DESC
            """,
            countQuery = "SELECT COUNT(*) FROM projects",
            nativeQuery = true)
    Page<Object[]> findAllProjectIdsWithTaskCount(Pageable pageable);

    /**
     * Returns a single row {@code [project_id, task_count]} for the given project,
     * or an empty list if the project does not exist.
     *
     * <p>Uses {@code List} instead of {@code Optional} to avoid the double-wrapping
     * issue that Hibernate has with {@code Optional<Object[]>} on native queries.</p>
     */
    @Query(value = """
            SELECT p.id       AS project_id,
                   COUNT(t.id) AS task_count
            FROM   projects p
            LEFT JOIN tasks t ON t.project_id = p.id
            WHERE  p.id = :id
            GROUP BY p.id
            """,
            nativeQuery = true)
    List<Object[]> findProjectIdWithTaskCountById(@Param("id") Long id);
}
