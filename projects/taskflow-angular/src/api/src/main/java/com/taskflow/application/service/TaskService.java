package com.taskflow.application.service;

import com.taskflow.application.exception.ResourceNotFoundException;
import com.taskflow.application.port.in.TaskUseCase;
import com.taskflow.application.port.out.ProjectOutputPort;
import com.taskflow.application.port.out.TaskOutputPort;
import com.taskflow.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service: implements all task use cases.
 * Only depends on ports — no JPA, no HTTP.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService implements TaskUseCase {

    private final TaskOutputPort    taskOutputPort;
    private final ProjectOutputPort projectOutputPort;

    // ---- Query ---------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public TasksPage listTasks(int page, int size, TaskFilter filter) {
        TaskOutputPort.TasksPage result = taskOutputPort.findAll(
                page, size,
                filter.projectId(),
                filter.status(),
                filter.priority(),
                filter.search());

        List<Task> tasks = result.items();
        return new TasksPage(tasks, result.total());
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTask(Long id) {
        return taskOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    // ---- Command -------------------------------------------------------------

    @Override
    public Task createTask(TaskCreateCommand cmd) {
        // Validate referenced project exists
        if (!projectOutputPort.existsById(cmd.projectId())) {
            throw new ResourceNotFoundException("Project", cmd.projectId());
        }

        Task task = Task.create(
                cmd.title(),
                cmd.description(),
                cmd.status(),
                cmd.priority(),
                cmd.projectId());

        return taskOutputPort.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskUpdateCommand cmd) {
        Task existing = taskOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        Task updated = existing;

        if (cmd.title() != null && !cmd.title().isBlank()) {
            updated = updated.withTitle(cmd.title());
        }
        if (cmd.description() != null) {
            updated = updated.withDescription(cmd.description());
        }
        if (cmd.status() != null) {
            updated = updated.withStatus(cmd.status());
        }
        if (cmd.priority() != null) {
            updated = updated.withPriority(cmd.priority());
        }
        if (cmd.projectId() != null) {
            // Validate the new project exists
            if (!projectOutputPort.existsById(cmd.projectId())) {
                throw new ResourceNotFoundException("Project", cmd.projectId());
            }
            updated = updated.withProjectId(cmd.projectId());
        }

        return taskOutputPort.save(updated);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskOutputPort.existsById(id)) {
            throw new ResourceNotFoundException("Task", id);
        }
        taskOutputPort.deleteById(id);
    }
}
