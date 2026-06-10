# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Spring Boot 3.5 / Java 17 REST API for an online book store. Stateless JWT authentication, MySQL persistence with Liquibase migrations, MapStruct DTO mapping, and Springdoc OpenAPI. Maven project (use the bundled `./mvnw` wrapper).

## Commands

```bash
# Build (runs checkstyle at compile phase + spotless apply)
./mvnw clean package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=BookRepositoryTest

# Run a single test method
./mvnw test -Dtest=BookRepositoryTest#findAllByCategoriesId_validCategoryId_returnsOneBook

# Run the app locally (expects MySQL on localhost:3306 per src/main/resources/application.properties)
./mvnw spring-boot:run
```

### Running via Docker

The whole stack (app + MySQL) runs in containers and is configured entirely through `.env`:

```bash
docker compose up -d          # build + start mysqldb and app
docker compose up -d mysqldb  # start only the database
docker ps                     # verify status/ports
```

Port mapping comes from `.env`, **not** `application.properties`:
- App: host **8088** → container 8080 (Postman/clients hit `http://localhost:8088`)
- MySQL: host **3307** → container 3306 (DBeaver/clients connect to `localhost:3307`, db `book_app`, user `root`, password from `MYSQLDB_ROOT_PASSWORD` in `.env`)
- Frontend (Angular/nginx): host **4200** → container 80 (`FRONTEND_LOCAL_PORT`); open `http://localhost:4200`
- Debug (JDWP): port **5005** is exposed for remote debugging

The backend `Dockerfile` is **multi-stage**: it builds the jar with Maven *inside* the image (`mvn package -DskipTests`), so `docker compose up -d` is fully self-contained — no local `./mvnw` build is needed first. After changing backend code, rebuild with `docker compose up -d --build app` (use `--no-cache` if a layer looks stale).

Note: `application.properties` (localhost:3306, password `password`) is for non-Docker local runs only and intentionally differs from the Docker `.env` values — a locally-run app will not reach the containerized DB without overriding these.

## Architecture

### Request flow & layering
`Controller (/api/**)` → `Service` (interface + `*Impl`) → `Repository` (Spring Data JPA) → `model` entities. Controllers speak DTOs only; **MapStruct mappers** (`mapper/`) convert between entities and DTOs. Mapper component model is set globally in `config/MapperConfig.java` (`componentModel = "spring"`), so mappers are injected as Spring beans.

Services are organized into per-domain subpackages (`service/book`, `service/order`, `service/shoppingcart`, etc.), each typically exposing an interface and an `*Impl`.

### Security
- `security/SecurityConfig.java` — stateless (`SessionCreationPolicy.STATELESS`), CSRF/CORS disabled, `@EnableMethodSecurity` on. Only `/api/auth/registration`, `/api/auth/login`, Swagger, and static assets are public; everything else requires authentication. Method-level authorization uses `@PreAuthorize` on controller/service methods.
- `security/filter/JwtAuthenticationFilter.java` runs before `UsernamePasswordAuthenticationFilter`, extracts the bearer token and populates the `SecurityContext`.
- `util/JwtUtil.java` — token generation/validation (jjwt 0.11.5). Secret and expiry come from `jwt.secret` / `jwt.expiration` properties.
- Passwords hashed with BCrypt.

To authenticate against the API: `POST /api/auth/login` → returns JWT → send as `Authorization: Bearer <token>` on subsequent requests.

### Dynamic book search (Specification pattern)
`GET /api/books/search` is built on a custom JPA Specification framework — when adding a new searchable field, add a provider rather than editing query logic:
- `repository/SpecificationProvider<T>` — interface; each impl has a `getKey()` (the field name) and `getSpecification(String[] params)`.
- `repository/book/spec/` — one `@Component` provider per searchable field (e.g. `AuthorSpecificationProvider`, `TitleSpecificationProvider`).
- `BookSpecificationProviderManager` — resolves a provider by key.
- `BookSpecificationBuilder` — assembles the combined `Specification<Book>` from search params.

### Validation
Custom cross-field validation lives in `validation/`: `@FieldMatch` annotation + `FieldMatchValidator` (used e.g. to confirm password == repeatPassword on registration).

### Exceptions
`exception/GlobalExceptionHandler.java` (`@RestControllerAdvice`) centralizes error responses for the custom exceptions in that package (`EntityNotFoundException`, `RegistrationException`, `EmptyCartException`, etc.).

### Database & migrations
Liquibase owns the schema; **Hibernate `ddl-auto=validate`** so the app will fail to start if entities and schema diverge. Migrations are in `src/main/resources/db/changelog/`:
- `db.changelog-master.yaml` includes numbered changesets in `changeset/` (`01-...` upward).
- Changesets cover both DDL (`create-*-table`) and seed data (`insert-default-*`).

**When changing an entity, add a new numbered changeset** — do not edit existing applied changesets, and never rely on Hibernate to alter the schema.

## Testing

- Repository tests use `@DataJpaTest` against a **real MySQL via Testcontainers** (not H2). `src/test/resources/application.properties` uses the JDBC-URL scheme `jdbc:tc:mysql:8:///...` so Testcontainers spins up the DB automatically; `@AutoConfigureTestDatabase(replace = NONE)` keeps the configured datasource. `config/CustomMySqlContainer.java` is a singleton container helper (note its `stop()` is intentionally a no-op for reuse).
- Test style: `// given / when / then` blocks with `@DisplayName`.

## Code style (enforced at build time)

- **Spotless** runs `apply` on build using Google Java Format, with import order `jakarta, javax, org, springframework, java`. Run `./mvnw spotless:apply` to auto-format.
- **Checkstyle** (`checkstyle.xml`) runs at the `compile` phase and **fails the build** on violations. Generated sources under `target/generated-sources/annotations` are suppressed via `checkstyle-suppressions.xml`.
- Lombok is used for boilerplate (constructors, getters/setters); it's excluded from the final Spring Boot jar.

## Frontend (`frontend/`)

Angular 22 SPA (standalone components, Angular Material, signals) that consumes this API.
See `frontend/` and the design at `docs/superpowers/specs/2026-06-05-angular-frontend-design.md`.

Key points for working across the stack:
- **Role detection depends on `GET /api/users/me`** (added here). The JWT only carries the
  email subject, so the frontend calls `/me` after login to learn the user's roles and
  drive role-based navigation/guards. If you change role semantics, keep `/me` in sync.
- **Same-origin in Docker**: `frontend/nginx.conf` reverse-proxies `/api` → `app:8080`, so the
  browser never makes a cross-origin call and the backend's disabled CORS is a non-issue.
  Locally, `ng serve` uses `frontend/proxy.conf.json` to proxy `/api` → `localhost:8088`.
- **Demo accounts** (seeded by changeset `21-set-demo-passwords`): `user@gmail.com / user12345`
  (USER) and `admin@gmail.com / admin12345` (USER + ADMIN).
- Frontend commands (run inside `frontend/`): `npm install`, `npm run build`, `npm start`
  (dev server on 4200), `npm test` (vitest). On this WSL/`/mnt/c` checkout npm does not create
  `node_modules/.bin`; invoke the CLI via `node node_modules/@angular/cli/bin/ng.js <cmd>`.
- `docker-compose.test.yml` is an isolated, port-conflict-free stack (only publishes 4200) for
  end-to-end smoke testing: `docker compose -p booktest -f docker-compose.test.yml up -d`.

## API docs

Swagger UI at `/swagger-ui/index.html`, OpenAPI JSON at `/v3/api-docs` (both public). Config in `config/OpenApiConfig.java`.
