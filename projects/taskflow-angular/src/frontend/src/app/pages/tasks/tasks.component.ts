import { Component, OnInit, signal, inject, DestroyRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { TaskService } from '../../services/task.service';
import { Task, TaskStatus, TaskPriority } from '../../models/types';
import { StatusBadgeComponent } from '../../components/badges/status-badge.component';
import { PriorityBadgeComponent } from '../../components/badges/priority-badge.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, StatusBadgeComponent, PriorityBadgeComponent, RouterLink],
  templateUrl: './tasks.component.html'
})
export class TasksComponent implements OnInit {
  private taskService = inject(TaskService);
  private fb = inject(FormBuilder);
  private destroyRef = inject(DestroyRef);

  tasks = signal<Task[]>([]);
  total = signal<number>(0);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  filterForm: FormGroup = this.fb.group({
    search: [''],
    status: ['' as TaskStatus | ''],
    priority: ['' as TaskPriority | '']
  });

  ngOnInit(): void {
    // Initial load
    this.fetchTasks();

    // Subscribe to filter changes with debounce
    this.filterForm.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.fetchTasks();
    });
  }

  private fetchTasks(): void {
    const { search, status, priority } = this.filterForm.value;
    this.loading.set(true);
    this.error.set(null);

    this.taskService.getTasks({
      search: search || undefined,
      status: status || undefined,
      priority: priority || undefined,
      size: 200
    }).subscribe({
      next: ({ tasks, total }) => {
        this.tasks.set(tasks);
        this.total.set(total);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to load tasks.');
        this.loading.set(false);
      }
    });
  }

  clearFilters(): void {
    this.filterForm.reset({ search: '', status: '', priority: '' });
  }

  get hasActiveFilters(): boolean {
    const { search, status, priority } = this.filterForm.value;
    return !!(search || status || priority);
  }
}
