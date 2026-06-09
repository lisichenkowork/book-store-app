import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

import { AuthService } from '../../core/auth.service';
import { CartService } from '../../core/cart.service';
import { OrderService } from '../../core/order.service';
import { CartItem, ShoppingCart } from '../../core/models';

@Component({
  selector: 'app-cart',
  imports: [
    FormsModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressBarModule,
  ],
  template: `
    <div class="page">
      <h2>Кошик</h2>
      @if (loading()) { <mat-progress-bar mode="indeterminate" /> }

      @if (!loading() && items().length === 0) {
        <p class="empty">Кошик порожній.</p>
      }

      @if (items().length > 0) {
        <table mat-table [dataSource]="items()" class="full-width">
          <ng-container matColumnDef="title">
            <th mat-header-cell *matHeaderCellDef>Книга</th>
            <td mat-cell *matCellDef="let item">{{ item.bookTitle }}</td>
          </ng-container>
          <ng-container matColumnDef="quantity">
            <th mat-header-cell *matHeaderCellDef>Кількість</th>
            <td mat-cell *matCellDef="let item">
              <div class="qty">
                <button mat-icon-button (click)="changeQty(item, item.quantity - 1)"
                        [disabled]="item.quantity <= 1">
                  <mat-icon>remove</mat-icon>
                </button>
                <span>{{ item.quantity }}</span>
                <button mat-icon-button (click)="changeQty(item, item.quantity + 1)">
                  <mat-icon>add</mat-icon>
                </button>
              </div>
            </td>
          </ng-container>
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef></th>
            <td mat-cell *matCellDef="let item">
              <button mat-icon-button color="warn" (click)="remove(item)">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>
          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns"></tr>
        </table>

        <mat-card class="checkout">
          <mat-card-header><mat-card-title>Оформлення замовлення</mat-card-title></mat-card-header>
          <mat-card-content>
            <mat-form-field class="full-width">
              <mat-label>Адреса доставки</mat-label>
              <input matInput [(ngModel)]="shippingAddress" />
            </mat-form-field>
            <button mat-raised-button color="primary"
                    [disabled]="!shippingAddress || placing()" (click)="placeOrder()">
              <mat-icon>shopping_bag</mat-icon> Оформити замовлення
            </button>
          </mat-card-content>
        </mat-card>
      }
    </div>
  `,
  styles: [
    `
      .empty { color: #888; margin-top: 24px; }
      .qty { display: flex; align-items: center; gap: 8px; }
      table { background: #fff; }
      .checkout { margin-top: 24px; max-width: 520px; }
    `,
  ],
})
export class CartComponent {
  private readonly cartService = inject(CartService);
  private readonly orderService = inject(OrderService);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly items = signal<CartItem[]>([]);
  readonly loading = signal(false);
  readonly placing = signal(false);
  readonly columns = ['title', 'quantity', 'actions'];
  shippingAddress = this.auth.currentUser()?.shippingAddress ?? '';

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.cartService.getCart().subscribe({
      next: (cart: ShoppingCart) => {
        this.items.set(cart.cartItems ?? []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  changeQty(item: CartItem, quantity: number): void {
    if (quantity < 1) {
      return;
    }
    this.cartService.updateQuantity(item.id, quantity).subscribe(() => this.load());
  }

  remove(item: CartItem): void {
    this.cartService.removeItem(item.id).subscribe(() => {
      this.snackBar.open('Позицію видалено', 'OK', { duration: 3000 });
      this.load();
    });
  }

  placeOrder(): void {
    this.placing.set(true);
    this.orderService.placeOrder({ shippingAddress: this.shippingAddress }).subscribe({
      next: () => {
        this.snackBar.open('Замовлення оформлено!', 'OK', { duration: 4000 });
        this.router.navigate(['/orders']);
      },
      error: () => this.placing.set(false),
    });
  }
}
