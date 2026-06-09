import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, switchMap, of, catchError } from 'rxjs';

import {
  CurrentUser,
  LoginRequest,
  LoginResponse,
  RegistrationRequest,
  Role,
} from './models';

const TOKEN_KEY = 'bookstore_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly tokenSig = signal<string | null>(localStorage.getItem(TOKEN_KEY));
  private readonly currentUserSig = signal<CurrentUser | null>(null);

  readonly currentUser = this.currentUserSig.asReadonly();
  readonly isLoggedIn = computed(() => this.tokenSig() !== null);
  readonly isAdmin = computed(() => this.hasRole('ADMIN'));
  readonly isUser = computed(() => this.hasRole('USER'));

  get token(): string | null {
    return this.tokenSig();
  }

  login(request: LoginRequest): Observable<CurrentUser> {
    return this.http.post<LoginResponse>('/api/auth/login', request).pipe(
      tap((res) => this.setToken(res.token)),
      switchMap(() => this.loadCurrentUser()),
    );
  }

  register(request: RegistrationRequest): Observable<unknown> {
    return this.http.post('/api/auth/registration', request);
  }

  loadCurrentUser(): Observable<CurrentUser> {
    return this.http
      .get<CurrentUser>('/api/users/me')
      .pipe(tap((user) => this.currentUserSig.set(user)));
  }

  /** Called on app start: if a token exists, restore the session. */
  restoreSession(): Observable<CurrentUser | null> {
    if (!this.tokenSig()) {
      return of(null);
    }
    return this.loadCurrentUser().pipe(
      catchError(() => {
        this.logout();
        return of(null);
      }),
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.tokenSig.set(null);
    this.currentUserSig.set(null);
  }

  private hasRole(role: Role): boolean {
    return this.currentUserSig()?.roles?.includes(role) ?? false;
  }

  private setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this.tokenSig.set(token);
  }
}
