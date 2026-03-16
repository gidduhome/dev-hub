import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-project-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-project-modal.component.html'
})
export class CreateProjectModalComponent implements OnChanges {
  @Input() visible = false;
  @Output() closed = new EventEmitter<void>();
  @Output() created = new EventEmitter<{ name: string; description?: string }>();

  private fb = inject(FormBuilder);

  form: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(200)]],
    description: ['']
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['visible'] && changes['visible'].currentValue === true) {
      this.form.reset({ name: '', description: '' });
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { name, description } = this.form.value;
    this.created.emit({ name: name.trim(), description: description?.trim() || undefined });
    this.form.reset({ name: '', description: '' });
  }

  get nameControl() {
    return this.form.get('name')!;
  }
}
