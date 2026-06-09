# Angular Frontend for Book Store — Design

Date: 2026-06-05
Status: Approved

## Goal

A containerized Angular SPA that talks to the existing Spring Boot book-store API,
supports login, and shows capabilities based on the logged-in user's role:
- ADMIN → endpoints guarded by `hasRole('ADMIN')` (book CRUD, category CRUD)
- USER → endpoints guarded by `hasRole('USER')` (cart, orders) + shared views

## Backend change (minimal)

The JWT ([JwtUtil]) only carries the email subject and the login response
([UserLoginResponseDto]) returns only `{ token }`. The frontend therefore cannot
determine the user's role. We add ONE endpoint:

```
GET /api/users/me  →  { id, email, firstName, lastName, shippingAddress, roles: ["USER","ADMIN"] }
```

- New `UserController` mapped at `/api/users`, method `me(Authentication)`.
- New `UserWithRolesDto` (UserResponseDto + `Set<String> roles`).
- `SecurityConfig` unchanged — `anyRequest().authenticated()` already covers it.

### Demo credentials
Registration only creates USER. To exercise ADMIN, a Liquibase changeset (`21-...`)
sets known BCrypt passwords for the seeded `user@gmail.com` and `admin@gmail.com`:
- user@gmail.com  / user12345
- admin@gmail.com / admin12345  (has USER + ADMIN)

## Frontend architecture (`frontend/`)

Latest stable Angular, standalone components, Angular Material, SCSS.

### Core (singletons)
- `AuthService` — `login()`, `register()`, `loadCurrentUser()` (GET /me), `logout()`;
  holds token + current user via signals; `isAdmin()`, `isUser()`, `isLoggedIn()`.
- `authInterceptor` — attaches `Authorization: Bearer <token>`.
- `errorInterceptor` — 401 → clear session + redirect `/login`; 403 → "no permission" toast.
- `authGuard` — requires login.
- `roleGuard(role)` — factory guard requiring a specific role.

### Features (standalone components)
- Auth: `LoginComponent`, `RegisterComponent`
- Books: `BookListComponent` (browse + search, all authed), `BookFormComponent` + delete (ADMIN)
- Categories: `CategoryListComponent` (view), `CategoryFormComponent` (ADMIN)
- Cart: `CartComponent` (USER)
- Orders: `OrderListComponent` + place order (USER)
- Layout: `ToolbarComponent` — role-conditional nav.

### Models
TypeScript interfaces mirroring backend DTOs (Book, Category, CartItem, ShoppingCart,
Order, OrderItem, auth request/response, user-with-roles).

## Routing & role access

| Route | Guard | Audience |
|---|---|---|
| `/login`, `/register` | — | all |
| `/books` | authGuard | any authed |
| `/books/new`, `/books/:id/edit` | roleGuard('ADMIN') | ADMIN |
| `/categories` | authGuard | authed |
| `/categories/new`, `/categories/:id/edit` | roleGuard('ADMIN') | ADMIN |
| `/cart` | roleGuard('USER') | USER |
| `/orders` | roleGuard('USER') | USER |

Toolbar renders admin/user links conditionally. Guards protect navigation;
real enforcement remains server-side via `@PreAuthorize`.

## Data flow & errors

1. Login → `{token}` → store → immediately `GET /api/users/me` → cache user+roles.
2. Every request passes through `authInterceptor`.
3. On app start, if a token exists in `localStorage`, call `/me` to restore the session;
   expired token (401) → clear + `/login`.
4. 403 from backend → friendly toast.

## Containerization

- `frontend/Dockerfile` — multi-stage: node build → nginx serves `dist/`.
- `frontend/nginx.conf` — SPA fallback + `location /api { proxy_pass http://app:8080; }`
  (same-origin → no CORS, backend untouched).
- `frontend/proxy.conf.json` — `ng serve` proxies `/api` → `http://localhost:8088`.
- New `frontend` service in `docker-compose.yml`: `build: ./frontend`, `depends_on: app`,
  host port `4200:80`.

Run: `docker compose up -d` → open `http://localhost:4200`.

## Testing (light)

Jasmine/Karma unit tests for the riskiest units: `AuthService` (login/me/logout/isAdmin)
and `roleGuard`.
