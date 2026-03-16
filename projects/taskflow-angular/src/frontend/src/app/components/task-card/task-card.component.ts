import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task, TaskStatus } from '../../models/types';
import { StatusBadgeComponent } from '../badges/status-badge.component';
import { PriorityBadgeComponent } from '../badges/priority-badge.component';

@Component({
  selector: 'app-task-card',
  standalone: true,
  imports: [CommonModule, StatusBadgeComponent, PriorityBadgeComponent],
  templateUrl: './task-card.component.html'
})
export class TaskCardComponent {
  @Input() task!: Task;
  @Output() statusChange = new EventEmitter<TaskStatus>();
  @Output() deleted = new EventEmitter<void>();

  cycleStatus(): void {
    const next: Record<TaskStatus, TaskStatus> = {
      TODO: 'IN_PROGRESS',
      IN_PROGRESS: 'DONE',
      DONE: 'TODO'
    };
    this.statusChange.emit(next[this.task.status]);
  }
}
