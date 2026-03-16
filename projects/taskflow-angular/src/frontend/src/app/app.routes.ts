import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'projects',
    loadComponent: () =>
      import('./pages/projects/projects.component').then(m => m.ProjectsComponent)
  },
  {
    path: 'projects/:id',
    loadComponent: () =>
      import('./pages/project-detail/project-detail.component').then(m => m.ProjectDetailComponent)
  },
  {
    path: 'tasks',
    loadComponent: () =>
      import('./pages/tasks/tasks.component').then(m => m.TasksComponent)
  },
];
