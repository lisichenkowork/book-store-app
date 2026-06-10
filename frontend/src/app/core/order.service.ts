import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CreateOrderRequest, Order } from './models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/orders';

  list(): Observable<Order[]> {
    return this.http.get<Order[]>(this.base);
  }

  placeOrder(request: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.base, request);
  }
}
