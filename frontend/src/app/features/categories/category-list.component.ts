import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

import { AuthService } from '../../core/auth.service';
import { CategoryService } from '../../core/category.service';
import { Category } from '../../core/models';

@Component({
  selector: 'app-category-list',
  imports: [
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
  ],
  template: `
    <div class="page">
      <div class="toolbar-row">
        <h2>Категорії</h2>
        @if (isAdmin()) {
          <a mat-raised-button color="primary" routerLink="/categories/new">
            <mat-icon>add</mat-icon> Нова категорія
          </a>
        }
      </div>

      @if (loading()) { <mat-progress-bar mode="indeterminate" /> }

      @if (!loading() && categories().length === 0) {
        <p class="empty">Категорій немає.</p>
      }

      @if (categories().length > 0) {
        <table mat-table [dataSource]="categories()" class="full-width">
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Назва</th>
            <td mat-cell *matCellDef="let c">{{ c.name }}</td>
          </ng-container>
          <ng-container matColumnDef="description">
            <th mat-header-cell *matHeaderCellDef>Опис</th>
            <td mat-cell *matCellDef="let c">{{ c.description }}</td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef></th>
            <td mat-cell *matCellDef="let c">
              @if (isAdmin()) {
                <a mat-icon-button [routerLink]="['/categories', c.id, 'edit']">
                  <mat-icon>edit</mat-icon>
                </a>
                <button mat-icon-button color="warn" (click)="remove(c)">
                  <mat-icon>delete</mat-icon>
                </button>
              }
            </td>
          </ng-container>
          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns"></tr>
        </table>
      }
    </div>
  `,
  styles: [
    `
      .toolbar-row { display: flex; align-items: center; gap: 16px; }
      .toolbar-row h2 { margin-right: auto; }
      .empty { color: #888; margin-top: 24px; }
      table { background: #fff; }
    `,
  ],
})
export class CategoryListComponent {
  private readonly categoryService = inject(CategoryService);
  private readonly auth = inject(AuthService);
  private readonly snackBar = inject(MatSnackBar);

  readonly isAdmin = this.auth.isAdmin;
  readonly categories = signal<Category[]>([]);
  readonly loading = signal(false);
  readonly columns = ['name', 'description', 'actions'];

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.categoryService.list().subscribe({
      next: (res) => {
        this.categories.set(res.content);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  remove(category: Category): void {
    if (!confirm(`Видалити категорію "${category.name}"?`)) {
      return;
    }
    this.categoryService.delete(category.id).subscribe(() => {
      this.snackBar.open('Категорію видалено', 'OK', { duration: 3000 });
      this.load();
    });
  }
}
