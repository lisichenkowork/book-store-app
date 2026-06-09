import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { CurrentUser } from './models';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [AuthService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('starts logged out', () => {
    expect(service.isLoggedIn()).toBe(false);
    expect(service.isAdmin()).toBe(false);
  });

  it('stores token and loads the current user on login', () => {
    const user: CurrentUser = {
      id: 1,
      email: 'admin@gmail.com',
      firstName: 'A',
      lastName: 'B',
      roles: ['USER', 'ADMIN'],
    };

    service.login({ email: 'admin@gmail.com', password: 'admin12345' }).subscribe();

    httpMock.expectOne('/api/auth/login').flush({ token: 'jwt-token' });
    httpMock.expectOne('/api/users/me').flush(user);

    expect(service.token).toBe('jwt-token');
    expect(service.isLoggedIn()).toBe(true);
    expect(service.isAdmin()).toBe(true);
    expect(service.isUser()).toBe(true);
  });

  it('isAdmin is false for a USER-only account', () => {
    const user: CurrentUser = {
      id: 2,
      email: 'user@gmail.com',
      firstName: 'U',
      lastName: 'S',
      roles: ['USER'],
    };

    service.login({ email: 'user@gmail.com', password: 'user12345' }).subscribe();
    httpMock.expectOne('/api/auth/login').flush({ token: 't' });
    httpMock.expectOne('/api/users/me').flush(user);

    expect(service.isUser()).toBe(true);
    expect(service.isAdmin()).toBe(false);
  });

  it('logout clears token and user', () => {
    service.login({ email: 'u@e.com', password: 'pass1234' }).subscribe();
    httpMock.expectOne('/api/auth/login').flush({ token: 't' });
    httpMock.expectOne('/api/users/me').flush({
      id: 1, email: 'u@e.com', firstName: 'a', lastName: 'b', roles: ['USER'],
    });

    service.logout();

    expect(service.isLoggedIn()).toBe(false);
    expect(service.currentUser()).toBeNull();
  });
});
