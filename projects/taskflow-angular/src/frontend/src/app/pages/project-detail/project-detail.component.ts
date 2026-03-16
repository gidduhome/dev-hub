import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { TaskService } from '../../services/task.service';
import { Project, Task, TaskPriority, TaskStatus } from '../../models/types';
import { CreateTaskFormComponent } from '../../components/create-task-form/create-task-form.component';
import { TaskCardComponent } from '../../components/task-card/task-card.component';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, CreateTaskFormComponent, TaskCardComponent],
  templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private projectService = inject(ProjectService);
  private taskService = inject(TaskService);

  project = signal<Project | null>(null);
  tasks = signal<Task[]>([]);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  todoTasks = computed(() => this.tasks().filter(t => t.status === 'TODO'));
  inProgressTasks = computed(() => this.tasks().filter(t => t.status === 'IN_PROGRESS'));
  doneTasks = computed(() => this.tasks().filter(t => t.status === 'DONE'));

  private get projectId(): number {
    return Number(this.route.snapshot.paramMap.get('id'));
  }

  ngOnInit(): void {
    this.load();
  }

  private load(): void {
    const id = this.projectId;
    this.loading.set(true);
    this.error.set(null);

    this.projectService.getProject(id).subscribe({
      next: (project) => {
        this.project.set(project);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to load project.');
        this.loading.set(false);
      }
    });

    this.taskService.getTasks({ projectId: id, size: 200 }).subscribe({
      next: ({ tasks }) => {
        this.tasks.set(tasks);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to load tasks.');
        this.loading.set(false);
      }
    });
  }

  onCreateTask(data: { title: string; priority: TaskPriority }): void {
    this.taskService.createTask({ ...data, projectId: this.projectId, status: 'TODO' }).subscribe({
      next: (task) => {
        this.tasks.update(tasks => [...tasks, task]);
        // Also bump the project task count
        this.project.update(p => p ? { ...p, taskCount: p.taskCount + 1 } : p);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to create task.');
      }
    });
  }

  onStatusChange(taskId: number, newStatus: TaskStatus): void {
    this.taskService.updateTask(taskId, { status: newStatus }).subscribe({
      next: (updated) => {
        this.tasks.update(tasks => tasks.map(t => t.id === updated.id ? updated : t));
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to update task.');
      }
    });
  }

  onDeleteTask(id: number): void {
    this.taskService.deleteTask(id).subscribe({
      next: () => {
        this.tasks.update(tasks => tasks.filter(t => t.id !== id));
        this.project.update(p => p ? { ...p, taskCount: Math.max(0, p.taskCount - 1) } : p);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to delete task.');
      }
    });
  }
}
