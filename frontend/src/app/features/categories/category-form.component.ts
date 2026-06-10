import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

import { CategoryService } from '../../core/category.service';

@Component({
  selector: 'app-category-form',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressBarModule,
  ],
  template: `
    <div class="page narrow">
      <mat-card>
        @if (loading()) { <mat-progress-bar mode="indeterminate" /> }
        <mat-card-header>
          <mat-card-title>{{ id ? 'Редагувати категорію' : 'Нова категорія' }}</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <mat-form-field class="full-width">
              <mat-label>Назва</mat-label>
              <input matInput formControlName="name" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Опис</mat-label>
              <textarea matInput rows="3" formControlName="description"></textarea>
            </mat-form-field>
            <div class="row-actions">
              <button mat-raised-button color="primary" type="submit"
                      [disabled]="form.invalid || loading()">Зберегти</button>
              <button mat-button type="button" (click)="cancel()">Скасувати</button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`.narrow { max-width: 480px; }`],
})
export class CategoryFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly categoryService = inject(CategoryService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly loading = signal(false);
  id?: number;

  readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    description: [''],
  });

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = Number(idParam);
      this.loading.set(true);
      this.categoryService.get(this.id).subscribe((category) => {
        this.form.patchValue({
          name: category.name,
          description: category.description ?? '',
        });
        this.loading.set(false);
      });
    }
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading.set(true);
    const payload = this.form.getRawValue();
    const request = this.id
      ? this.categoryService.update(this.id, payload)
      : this.categoryService.create(payload);
    request.subscribe({
      next: () => {
        this.snackBar.open('Категорію збережено', 'OK', { duration: 3000 });
        this.router.navigate(['/categories']);
      },
      error: () => this.loading.set(false),
    });
  }

  cancel(): void {
    this.router.navigate(['/categories']);
  }
}
