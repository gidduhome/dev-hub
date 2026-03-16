package com.taskflow.infrastructure.persistence.adapter;

import com.taskflow.application.port.out.TaskOutputPort;
import com.taskflow.domain.model.Task;
import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;
import com.taskflow.infrastructure.persistence.entity.TaskEntity;
import com.taskflow.infrastructure.persistence.repository.TaskJpaRepository;
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
public class TaskPersistenceAdapter implements TaskOutputPort {

    private final TaskJpaRepository jpaRepository;

    @Override
    public TasksPage findAll(int page, int size,
                             Long projectId,
                             TaskStatus status,
                             TaskPriority priority,
                             String search) {
        PageRequest pageable = PageRequest.of(page, size);

        // Normalise blank search string to null so the JPQL predicate skips it
        String normalizedSearch = (search != null && search.isBlank()) ? null : search;

        Page<TaskEntity> result = jpaRepository.findAllWithFilters(
                projectId, status, priority, normalizedSearch, pageable);

        List<Task> tasks = result.getContent().stream()
                .map(TaskEntity::toDomain)
                .toList();

        return new TasksPage(tasks, result.getTotalElements());
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaRepository.findById(id).map(TaskEntity::toDomain);
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = TaskEntity.fromDomain(task);
        TaskEntity saved  = jpaRepository.save(entity);
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
}
