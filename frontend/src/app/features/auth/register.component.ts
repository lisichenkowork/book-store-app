import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-register',
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
        <mat-card-header><mat-card-title>Реєстрація</mat-card-title></mat-card-header>
        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <mat-form-field class="full-width">
              <mat-label>Email</mat-label>
              <input matInput type="email" formControlName="email" />
            </mat-form-field>
            <div class="two-col">
              <mat-form-field>
                <mat-label>Ім'я</mat-label>
                <input matInput formControlName="firstName" />
              </mat-form-field>
              <mat-form-field>
                <mat-label>Прізвище</mat-label>
                <input matInput formControlName="lastName" />
              </mat-form-field>
            </div>
            <mat-form-field class="full-width">
              <mat-label>Адреса доставки</mat-label>
              <input matInput formControlName="shippingAddress" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Пароль</mat-label>
              <input matInput type="password" formControlName="password" />
            </mat-form-field>
            <mat-form-field class="full-width">
              <mat-label>Повторіть пароль</mat-label>
              <input matInput type="password" formControlName="repeatPassword" />
            </mat-form-field>
            @if (error()) { <p class="error">{{ error() }}</p> }
            <button mat-raised-button color="primary" class="full-width"
                    type="submit" [disabled]="form.invalid || loading()">
              Зареєструватися
            </button>
          </form>
          <p class="hint">Вже маєте акаунт? <a routerLink="/login">Увійти</a></p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [
    `
      .narrow { max-width: 480px; }
      .two-col { display: flex; gap: 12px; }
      .two-col mat-form-field { flex: 1; }
      .error { color: #c62828; margin: 4px 0 12px; }
      .hint { margin-top: 12px; font-size: 13px; }
    `,
  ],
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', [Validators.required, Validators.minLength(2)]],
    lastName: ['', [Validators.required, Validators.minLength(2)]],
    shippingAddress: [''],
    password: ['', [Validators.required, Validators.minLength(8)]],
    repeatPassword: ['', [Validators.required]],
  });

  submit(): void {
    this.error.set(null);
    const value = this.form.getRawValue();
    if (value.password !== value.repeatPassword) {
      this.error.set('Паролі не співпадають');
      return;
    }
    this.loading.set(true);
    this.auth.register(value).subscribe({
      next: () => {
        this.snackBar.open('Акаунт створено. Тепер увійдіть.', 'OK', { duration: 4000 });
        this.router.navigate(['/login']);
      },
      error: () => {
        this.error.set('Не вдалося зареєструватися (можливо, email вже існує)');
        this.loading.set(false);
      },
    });
  }
}
