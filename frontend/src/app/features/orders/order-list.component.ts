import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { OrderService } from '../../core/order.service';
import { Order } from '../../core/models';

@Component({
  selector: 'app-order-list',
  imports: [CurrencyPipe, DatePipe, MatCardModule, MatTableModule, MatChipsModule, MatProgressBarModule],
  template: `
    <div class="page">
      <h2>Мої замовлення</h2>
      @if (loading()) { <mat-progress-bar mode="indeterminate" /> }

      @if (!loading() && orders().length === 0) {
        <p class="empty">У вас ще немає замовлень.</p>
      }

      @for (order of orders(); track order.id) {
        <mat-card class="order">
          <mat-card-header>
            <mat-card-title>Замовлення #{{ order.id }}</mat-card-title>
            <mat-card-subtitle>
              {{ order.orderDate | date: 'dd.MM.yyyy HH:mm' }} ·
              {{ order.shippingAddress }}
            </mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <mat-chip-set>
              <mat-chip>{{ order.status }}</mat-chip>
              <mat-chip>Сума: {{ order.total | currency: 'UAH' : 'symbol-narrow' }}</mat-chip>
            </mat-chip-set>

            <table mat-table [dataSource]="order.orderItems" class="full-width items">
              <ng-container matColumnDef="bookId">
                <th mat-header-cell *matHeaderCellDef>Книга (ID)</th>
                <td mat-cell *matCellDef="let it">{{ it.bookId }}</td>
              </ng-container>
              <ng-container matColumnDef="quantity">
                <th mat-header-cell *matHeaderCellDef>Кількість</th>
                <td mat-cell *matCellDef="let it">{{ it.quantity }}</td>
              </ng-container>
              <ng-container matColumnDef="price">
                <th mat-header-cell *matHeaderCellDef>Ціна</th>
                <td mat-cell *matCellDef="let it">
                  {{ it.price | currency: 'UAH' : 'symbol-narrow' }}
                </td>
              </ng-container>
              <tr mat-header-row *matHeaderRowDef="itemColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: itemColumns"></tr>
            </table>
          </mat-card-content>
        </mat-card>
      }
    </div>
  `,
  styles: [
    `
      .empty { color: #888; margin-top: 24px; }
      .order { margin-bottom: 16px; }
      .items { margin-top: 12px; background: #fafafa; }
    `,
  ],
})
export class OrderListComponent {
  private readonly orderService = inject(OrderService);

  readonly orders = signal<Order[]>([]);
  readonly loading = signal(false);
  readonly itemColumns = ['bookId', 'quantity', 'price'];

  constructor() {
    this.loading.set(true);
    this.orderService.list().subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
