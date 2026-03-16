import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { Task, TaskPriority, TaskStatus } from '../models/types';

export interface TaskFilters {
  projectId?: number;
  status?: TaskStatus | '';
  priority?: TaskPriority | '';
  search?: string;
  page?: number;
  size?: number;
}

@Injectable({ providedIn: 'root' })
export class TaskService {
  private api = inject(ApiService);

  getTasks(filters: TaskFilters = {}): Observable<{ tasks: Task[]; total: number }> {
    const params: Record<string, string | number | boolean> = {};
    if (filters.page !== undefined) params['page'] = filters.page;
    if (filters.size !== undefined) params['size'] = filters.size;
    if (filters.projectId !== undefined) params['projectId'] = filters.projectId;
    if (filters.status) params['status'] = filters.status;
    if (filters.priority) params['priority'] = filters.priority;
    if (filters.search) params['search'] = filters.search;

    return this.api.get<Task[]>('/tasks', params).pipe(
      map(res => ({
        tasks: res.data,
        total: res.meta.total ?? res.data.length
      }))
    );
  }

  getTask(id: number): Observable<Task> {
    return this.api.get<Task>(`/tasks/${id}`).pipe(
      map(res => res.data)
    );
  }

  createTask(data: {
    title: string;
    description?: string;
    status?: TaskStatus;
    priority?: TaskPriority;
    projectId: number;
  }): Observable<Task> {
    return this.api.post<Task>('/tasks', data).pipe(
      map(res => res.data)
    );
  }

  updateTask(
    id: number,
    data: Partial<{ title: string; description: string; status: TaskStatus; priority: TaskPriority }>
  ): Observable<Task> {
    return this.api.patch<Task>(`/tasks/${id}`, data).pipe(
      map(res => res.data)
    );
  }

  deleteTask(id: number): Observable<void> {
    return this.api.delete<void>(`/tasks/${id}`).pipe(
      map(() => undefined)
    );
  }
}
