import { Routes } from '@angular/router';

import { authGuard, roleGuard } from './core/guards';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'books' },

  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register.component').then((m) => m.RegisterComponent),
  },

  {
    path: 'books',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/books/book-list.component').then((m) => m.BookListComponent),
  },
  {
    path: 'books/new',
    canActivate: [roleGuard('ADMIN')],
    loadComponent: () =>
      import('./features/books/book-form.component').then((m) => m.BookFormComponent),
  },
  {
    path: 'books/:id/edit',
    canActivate: [roleGuard('ADMIN')],
    loadComponent: () =>
      import('./features/books/book-form.component').then((m) => m.BookFormComponent),
  },

  {
    path: 'categories',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/categories/category-list.component').then((m) => m.CategoryListComponent),
  },
  {
    path: 'categories/new',
    canActivate: [roleGuard('ADMIN')],
    loadComponent: () =>
      import('./features/categories/category-form.component').then((m) => m.CategoryFormComponent),
  },
  {
    path: 'categories/:id/edit',
    canActivate: [roleGuard('ADMIN')],
    loadComponent: () =>
      import('./features/categories/category-form.component').then((m) => m.CategoryFormComponent),
  },

  {
    path: 'cart',
    canActivate: [roleGuard('USER')],
    loadComponent: () => import('./features/cart/cart.component').then((m) => m.CartComponent),
  },
  {
    path: 'orders',
    canActivate: [roleGuard('USER')],
    loadComponent: () =>
      import('./features/orders/order-list.component').then((m) => m.OrderListComponent),
  },

  { path: '**', redirectTo: 'books' },
];
