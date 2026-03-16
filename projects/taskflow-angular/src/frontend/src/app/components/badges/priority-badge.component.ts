import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskPriority } from '../../models/types';

@Component({
  selector: 'app-priority-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge" [ngClass]="colorClass">{{ label }}</span>
  `
})
export class PriorityBadgeComponent {
  @Input() priority!: TaskPriority;

  get label(): string {
    switch (this.priority) {
      case 'LOW': return 'Low';
      case 'MEDIUM': return 'Medium';
      case 'HIGH': return 'High';
      default: return this.priority;
    }
  }

  get colorClass(): string {
    switch (this.priority) {
      case 'LOW': return 'bg-slate-100 text-slate-600';
      case 'MEDIUM': return 'bg-yellow-100 text-yellow-700';
      case 'HIGH': return 'bg-red-100 text-red-700';
      default: return 'bg-slate-100 text-slate-600';
    }
  }
}
