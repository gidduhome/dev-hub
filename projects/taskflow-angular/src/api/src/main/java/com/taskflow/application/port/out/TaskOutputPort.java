package com.taskflow.application.port.out;

import com.taskflow.domain.model.Task;
import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;

import java.util.List;
import java.util.Optional;

/**
 * Output port: persistence operations for tasks.
 */
public interface TaskOutputPort {

    record TasksPage(List<Task> items, long total) {}

    TasksPage findAll(int page, int size,
                      Long projectId,
                      TaskStatus status,
                      TaskPriority priority,
                      String search);

    Optional<Task> findById(Long id);

    Task save(Task task);

    void deleteById(Long id);

    boolean existsById(Long id);
}
