import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TaskPriority } from '../../models/types';

@Component({
  selector: 'app-create-task-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-task-form.component.html'
})
export class CreateTaskFormComponent {
  @Input() projectId!: number;
  @Output() taskCreated = new EventEmitter<{ title: string; priority: TaskPriority }>();

  private fb = inject(FormBuilder);

  form: FormGroup = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(500)]],
    priority: ['MEDIUM' as TaskPriority, Validators.required]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { title, priority } = this.form.value;
    this.taskCreated.emit({ title: title.trim(), priority });
    this.form.reset({ title: '', priority: 'MEDIUM' });
  }

  get titleControl() {
    return this.form.get('title')!;
  }
}
