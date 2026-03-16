package com.taskflow.infrastructure.persistence.adapter;

import com.taskflow.application.port.out.ProjectOutputPort;
import com.taskflow.domain.model.Project;
import com.taskflow.infrastructure.persistence.entity.ProjectEntity;
import com.taskflow.infrastructure.persistence.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter: translates between the domain output port and Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectOutputPort {

    private final ProjectJpaRepository jpaRepository;

    @Override
    public ProjectsPage findAllWithTaskCount(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<Object[]> rawPage = jpaRepository.findAllProjectIdsWithTaskCount(pageable);

        List<ProjectWithCount> items = rawPage.getContent().stream()
                .map(row -> {
                    Long projectId = toLong(row[0]);
                    long taskCount = toLong(row[1]);

                    ProjectEntity entity = jpaRepository.findById(projectId)
                            .orElseThrow(() -> new IllegalStateException(
                                    "Project not found after count query: " + projectId));

                    return new ProjectWithCount(entity.toDomain(), taskCount);
                })
                .toList();

        return new ProjectsPage(items, rawPage.getTotalElements());
    }

    @Override
    public Optional<Project> findById(Long id) {
        return jpaRepository.findById(id).map(ProjectEntity::toDomain);
    }

    @Override
    public Optional<ProjectWithCount> findByIdWithTaskCount(Long id) {
        List<Object[]> results = jpaRepository.findProjectIdWithTaskCountById(id);
        if (results.isEmpty()) {
            return Optional.empty();
        }

        Object[] row = results.get(0);
        Long projectId = toLong(row[0]);
        long taskCount = toLong(row[1]);

        ProjectEntity entity = jpaRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException(
                        "Project not found after count query: " + projectId));

        return Optional.of(new ProjectWithCount(entity.toDomain(), taskCount));
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity  = ProjectEntity.fromDomain(project);
        ProjectEntity saved   = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    // ---- Helpers --------------------------------------------------------------

    /**
     * Native query columns come back as {@link Number} subtypes (BigInteger, Long, etc.)
     * depending on the JDBC driver.  This utility normalises them to {@code long}.
     */
    private static long toLong(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        }
        throw new IllegalArgumentException("Cannot convert to long: " + value);
    }
}
