import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskStatus } from '../../models/types';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge" [ngClass]="colorClass">{{ label }}</span>
  `
})
export class StatusBadgeComponent {
  @Input() status!: TaskStatus;

  get label(): string {
    switch (this.status) {
      case 'TODO': return 'To Do';
      case 'IN_PROGRESS': return 'In Progress';
      case 'DONE': return 'Done';
      default: return this.status;
    }
  }

  get colorClass(): string {
    switch (this.status) {
      case 'TODO': return 'bg-slate-100 text-slate-600';
      case 'IN_PROGRESS': return 'bg-blue-100 text-blue-700';
      case 'DONE': return 'bg-green-100 text-green-700';
      default: return 'bg-slate-100 text-slate-600';
    }
  }
}
