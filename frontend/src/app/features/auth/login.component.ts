import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressBarModule,
  ],
  template: `
    <div class="page narrow">
      <mat-card>
        @if (loading()) { <mat-progress-bar mode="indeterminate" /> }
        <mat-card-header><mat-card-title>Вхід</mat-card-title></mat-card-header>
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <mat-form-field class="full-width">
              <mat-label>Email</mat-label>
              <input matInput type="email" formControlName="email" autocomplete="username" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Пароль</mat-label>
              <input matInput type="password" formControlName="password"
                     autocomplete="current-password" />
            </mat-form-field>
            @if (error()) { <p class="error">{{ error() }}</p> }
            <button mat-raised-button color="primary" class="full-width"
                    type="submit" [disabled]="form.invalid || loading()">
              Увійти
            </button>
          </form>
          <p class="hint">
            Немає акаунту? <a routerLink="/register">Зареєструватися</a>
          </p>
          <p class="demo">
            Демо: <code>user&#64;gmail.com / user12345</code> ·
            <code>admin&#64;gmail.com / admin12345</code>
          </p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [
    `
      .narrow { max-width: 420px; }
      .error { color: #c62828; margin: 4px 0 12px; }
      .hint, .demo { margin-top: 12px; font-size: 13px; }
      .demo code { background: #eee; padding: 1px 4px; border-radius: 4px; }
    `,
  ],
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/books']),
      error: () => {
        this.error.set('Невірний email або пароль');
        this.loading.set(false);
      },
    });
  }
}
