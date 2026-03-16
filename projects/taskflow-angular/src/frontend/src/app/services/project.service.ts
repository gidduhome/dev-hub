import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ApiService } from './api.service';
import { Project } from '../models/types';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private api = inject(ApiService);

  getProjects(page = 0, size = 20): Observable<{ projects: Project[]; total: number }> {
    return this.api.get<Project[]>('/projects', { page, size }).pipe(
      map(res => ({
        projects: res.data,
        total: res.meta.total ?? res.data.length
      }))
    );
  }

  getProject(id: number): Observable<Project> {
    return this.api.get<Project>(`/projects/${id}`).pipe(
      map(res => res.data)
    );
  }

  createProject(data: { name: string; description?: string }): Observable<Project> {
    return this.api.post<Project>('/projects', data).pipe(
      map(res => res.data)
    );
  }

  updateProject(id: number, data: Partial<{ name: string; description: string }>): Observable<Project> {
    return this.api.patch<Project>(`/projects/${id}`, data).pipe(
      map(res => res.data)
    );
  }

  deleteProject(id: number): Observable<void> {
    return this.api.delete<void>(`/projects/${id}`).pipe(
      map(() => undefined)
    );
  }
}
