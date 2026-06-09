import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

import { BookService } from '../../core/book.service';
import { CategoryService } from '../../core/category.service';
import { Category } from '../../core/models';

@Component({
  selector: 'app-book-form',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatProgressBarModule,
  ],
  template: `
    <div class="page narrow">
      <mat-card>
        @if (loading()) { <mat-progress-bar mode="indeterminate" /> }
        <mat-card-header>
          <mat-card-title>{{ id ? 'Редагувати книгу' : 'Нова книга' }}</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <mat-form-field class="full-width">
              <mat-label>Назва</mat-label>
              <input matInput formControlName="title" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Автор</mat-label>
              <input matInput formControlName="author" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>ISBN</mat-label>
              <input matInput formControlName="isbn" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Ціна</mat-label>
              <input matInput type="number" step="0.01" formControlName="price" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Опис</mat-label>
              <textarea matInput rows="3" formControlName="description"></textarea>
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>URL обкладинки</mat-label>
              <input matInput formControlName="coverImage" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Категорії</mat-label>
              <mat-select formControlName="categories" multiple>
                @for (cat of categories(); track cat.id) {
                  <mat-option [value]="cat.id">{{ cat.name }}</mat-option>
                }
              </mat-select>
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
  styles: [`.narrow { max-width: 560px; }`],
})
export class BookFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly bookService = inject(BookService);
  private readonly categoryService = inject(CategoryService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly categories = signal<Category[]>([]);
  readonly loading = signal(false);
  id?: number;

  readonly form = this.fb.nonNullable.group({
    title: ['', Validators.required],
    author: ['', Validators.required],
    isbn: ['', Validators.required],
    price: [0, [Validators.required, Validators.min(0.01)]],
    description: ['', Validators.required],
    coverImage: ['', Validators.required],
    categories: [[] as number[], Validators.required],
  });

  constructor() {
    this.categoryService.list().subscribe((res) => this.categories.set(res.content));

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = Number(idParam);
      this.loading.set(true);
      this.bookService.get(this.id).subscribe((book) => {
        this.form.patchValue({
          title: book.title,
          author: book.author,
          isbn: book.isbn,
          price: book.price,
          description: book.description ?? '',
          coverImage: book.coverImage ?? '',
          categories: book.categories ?? [],
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
      ? this.bookService.update(this.id, payload)
      : this.bookService.create(payload);
    request.subscribe({
      next: () => {
        this.snackBar.open('Книгу збережено', 'OK', { duration: 3000 });
        this.router.navigate(['/books']);
      },
      error: () => this.loading.set(false),
    });
  }

  cancel(): void {
    this.router.navigate(['/books']);
  }
}
