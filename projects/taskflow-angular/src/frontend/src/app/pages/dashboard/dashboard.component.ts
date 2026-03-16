import { Component, OnInit, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { TaskService } from '../../services/task.service';
import { Project, Task } from '../../models/types';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private projectService = inject(ProjectService);
  private taskService = inject(TaskService);

  recentProjects = signal<Project[]>([]);
  totalProjects = signal<number>(0);
  allTasks = signal<Task[]>([]);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  totalTasks = computed(() => this.allTasks().length);
  inProgressCount = computed(() => this.allTasks().filter(t => t.status === 'IN_PROGRESS').length);
  doneCount = computed(() => this.allTasks().filter(t => t.status === 'DONE').length);

  ngOnInit(): void {
    this.load();
  }

  private load(): void {
    this.loading.set(true);
    this.error.set(null);

    this.projectService.getProjects(0, 5).subscribe({
      next: ({ projects, total }) => {
        this.recentProjects.set(projects);
        this.totalProjects.set(total);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to load projects.');
      }
    });

    this.taskService.getTasks({ size: 1000 }).subscribe({
      next: ({ tasks }) => {
        this.allTasks.set(tasks);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to load tasks.');
        this.loading.set(false);
      }
    });
  }
}
