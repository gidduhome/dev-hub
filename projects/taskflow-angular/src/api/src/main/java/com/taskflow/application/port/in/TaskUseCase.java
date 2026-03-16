package com.taskflow.application.port.in;

import com.taskflow.domain.model.Task;
import com.taskflow.domain.model.TaskPriority;
import com.taskflow.domain.model.TaskStatus;

import java.util.List;

/**
 * Input port: all task-related use cases.
 */
public interface TaskUseCase {

    // ---- Nested records -------------------------------------------------------

    record TasksPage(List<Task> tasks, long total) {}

    record TaskFilter(Long projectId, TaskStatus status, TaskPriority priority, String search) {
        public static TaskFilter empty() {
            return new TaskFilter(null, null, null, null);
        }
    }

    record TaskCreateCommand(String title, String description,
                             TaskStatus status, TaskPriority priority,
                             Long projectId) {}

    record TaskUpdateCommand(String title, String description,
                             TaskStatus status, TaskPriority priority,
                             Long projectId) {}

    // ---- Use case methods -----------------------------------------------------

    TasksPage listTasks(int page, int size, TaskFilter filter);

    Task getTask(Long id);

    Task createTask(TaskCreateCommand cmd);

    Task updateTask(Long id, TaskUpdateCommand cmd);

    void deleteTask(Long id);
}
