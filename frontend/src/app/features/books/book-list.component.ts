import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

import { AuthService } from '../../core/auth.service';
import { BookService } from '../../core/book.service';
import { CartService } from '../../core/cart.service';
import { Book } from '../../core/models';

@Component({
  selector: 'app-book-list',
  imports: [
    CurrencyPipe,
    FormsModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatProgressBarModule,
  ],
  template: `
    <div class="page">
      <div class="toolbar-row">
        <h2>Книги</h2>
        <mat-form-field class="search" subscriptSizing="dynamic">
          <mat-label>Пошук за назвою або автором</mat-label>
          <input matInput [(ngModel)]="term" (keyup.enter)="search()" />
          <button matSuffix mat-icon-button (click)="search()"><mat-icon>search</mat-icon></button>
        </mat-form-field>
        @if (term) {
          <button mat-button (click)="clearSearch()">Скинути</button>
        }
      </div>

      @if (loading()) { <mat-progress-bar mode="indeterminate" /> }

      @if (!loading() && books().length === 0) {
        <p class="empty">Книг не знайдено.</p>
      }

      <div class="grid">
        @for (book of books(); track book.id) {
          <mat-card>
            <mat-card-header>
              <mat-card-title>{{ book.title }}</mat-card-title>
              <mat-card-subtitle>{{ book.author }}</mat-card-subtitle>
            </mat-card-header>
            <mat-card-content>
              <p class="desc">{{ book.description }}</p>
              <p class="price">{{ book.price | currency: 'UAH' : 'symbol-narrow' }}</p>
            </mat-card-content>
            <mat-card-actions>
              @if (isUser()) {
                <button mat-button color="primary" (click)="addToCart(book)">
                  <mat-icon>add_shopping_cart</mat-icon> В кошик
                </button>
              }
              @if (isAdmin()) {
                <a mat-button [routerLink]="['/books', book.id, 'edit']">
                  <mat-icon>edit</mat-icon>
                </a>
                <button mat-button color="warn" (click)="remove(book)">
                  <mat-icon>delete</mat-icon>
                </button>
              }
            </mat-card-actions>
          </mat-card>
        }
      </div>

      @if (total() > 0) {
        <mat-paginator
          [length]="total()"
          [pageSize]="size"
          [pageIndex]="page()"
          [pageSizeOptions]="[6, 12, 24]"
          (page)="onPage($event)" />
      }
    </div>
  `,
  styles: [
    `
      .toolbar-row { display: flex; align-items: center; gap: 16px; flex-wrap: wrap; }
      .toolbar-row h2 { margin-right: auto; }
      .search { min-width: 320px; }
      .grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
        gap: 16px;
        margin-top: 16px;
      }
      .desc {
        color: #666; font-size: 13px;
        display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical;
        overflow: hidden;
      }
      .price { font-weight: 700; font-size: 16px; }
      .empty { color: #888; margin-top: 24px; }
    `,
  ],
})
export class BookListComponent {
  private readonly bookService = inject(BookService);
  private readonly cartService = inject(CartService);
  private readonly auth = inject(AuthService);
  private readonly snackBar = inject(MatSnackBar);

  readonly isUser = this.auth.isUser;
  readonly isAdmin = this.auth.isAdmin;

  readonly books = signal<Book[]>([]);
  readonly total = signal(0);
  readonly page = signal(0);
  readonly loading = signal(false);
  size = 12;
  term = '';

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    const request = this.term
      ? this.bookService.search(this.term, this.page(), this.size)
      : this.bookService.list(this.page(), this.size);
    request.subscribe({
      next: (res) => {
        this.books.set(res.content);
        this.total.set(res.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  search(): void {
    this.page.set(0);
    this.load();
  }

  clearSearch(): void {
    this.term = '';
    this.page.set(0);
    this.load();
  }

  onPage(event: PageEvent): void {
    this.page.set(event.pageIndex);
    this.size = event.pageSize;
    this.load();
  }

  addToCart(book: Book): void {
    this.cartService.addItem({ bookId: book.id, quantity: 1 }).subscribe(() =>
      this.snackBar.open(`"${book.title}" додано в кошик`, 'OK', { duration: 3000 }),
    );
  }

  remove(book: Book): void {
    if (!confirm(`Видалити "${book.title}"?`)) {
      return;
    }
    this.bookService.delete(book.id).subscribe(() => {
      this.snackBar.open('Книгу видалено', 'OK', { duration: 3000 });
      this.load();
    });
  }
}
