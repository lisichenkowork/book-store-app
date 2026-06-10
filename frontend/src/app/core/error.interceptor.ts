import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

import { AuthService } from './auth.service';

/**
 * Centralized HTTP error handling:
 *  - 401 → clear session and redirect to /login
 *  - 403 → friendly "no permission" toast
 *  - other → surface backend message as a toast
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        auth.logout();
        // Avoid bouncing the /me probe to a toast; just send to login.
        if (!req.url.includes('/api/auth/')) {
          router.navigate(['/login']);
        }
      } else if (error.status === 403) {
        snackBar.open('У вас немає прав для цієї дії', 'OK', { duration: 4000 });
      } else if (error.status > 0 && !req.url.includes('/api/auth/')) {
        snackBar.open(extractMessage(error), 'OK', { duration: 4000 });
      }
      return throwError(() => error);
    }),
  );
};

function extractMessage(error: HttpErrorResponse): string {
  const body = error.error;
  if (typeof body === 'string') {
    return body;
  }
  if (body?.message) {
    return body.message;
  }
  if (body?.errors && Array.isArray(body.errors)) {
    return body.errors.join(', ');
  }
  return `Помилка ${error.status}`;
}
