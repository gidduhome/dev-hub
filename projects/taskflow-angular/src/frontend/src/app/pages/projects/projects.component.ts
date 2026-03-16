import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/types';
import { CreateProjectModalComponent } from '../../components/create-project-modal/create-project-modal.component';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, RouterLink, CreateProjectModalComponent],
  templateUrl: './projects.component.html'
})
export class ProjectsComponent implements OnInit {
  private projectService = inject(ProjectService);

  projects = signal<Project[]>([]);
  showModal = signal<boolean>(false);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadProjects();
  }

  private loadProjects(): void {
    this.loading.set(true);
    this.error.set(null);
    this.projectService.getProjects(0, 100).subscribe({
      next: ({ projects }) => {
        this.projects.set(projects);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to load projects.');
        this.loading.set(false);
      }
    });
  }

  onCreateProject(data: { name: string; description?: string }): void {
    this.projectService.createProject(data).subscribe({
      next: () => {
        this.showModal.set(false);
        this.loadProjects();
      },
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to create project.');
      }
    });
  }

  onDeleteProject(id: number): void {
    if (!confirm('Delete this project and all its tasks? This cannot be undone.')) return;
    this.projectService.deleteProject(id).subscribe({
      next: () => this.loadProjects(),
      error: (err) => {
        this.error.set(err?.message ?? 'Failed to delete project.');
      }
    });
  }
}
