import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AddToCartRequest, CartItem, ShoppingCart } from './models';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/cart';

  getCart(): Observable<ShoppingCart> {
    return this.http.get<ShoppingCart>(this.base);
  }

  addItem(request: AddToCartRequest): Observable<CartItem> {
    return this.http.post<CartItem>(this.base, request);
  }

  updateQuantity(cartItemId: number, quantity: number): Observable<CartItem> {
    return this.http.put<CartItem>(`${this.base}/cart-items/${cartItemId}`, { quantity });
  }

  removeItem(cartItemId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/cart-items/${cartItemId}`);
  }
}
