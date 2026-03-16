# TaskFlow Angular

A task management application built with **Angular 17** (frontend) and **Spring Boot 3** (backend), demonstrating **Hexagonal Architecture** (ports & adapters). This project mirrors the `taskflow` project in functionality but uses a completely different tech stack and architectural pattern.

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 17 + TypeScript + TailwindCSS |
| State | Angular Signals + RxJS |
| Forms | Angular Reactive Forms |
| Backend | Spring Boot 3.x + Java 21 |
| Architecture | Hexagonal (Ports & Adapters) |
| ORM | Spring Data JPA + Hibernate |
| Migrations | Flyway |
| Database | PostgreSQL 16 |
| Docs | Springdoc OpenAPI (Swagger UI) |
| Container | Docker + Docker Compose |
| Testing | JUnit 5 + Spring Boot Test / Jasmine + Karma |

## Architecture Overview

The backend uses **Hexagonal Architecture** — the domain and application logic have zero framework dependencies. Frameworks (Spring, JPA) are pushed to the edges as adapters.

```
Web Request
    ↓
infrastructure/web/controller/  (Web Adapter — Spring @RestController)
    ↓
application/port/in/            (Input Port — Java interface)
    ↓
application/service/            (Use Case — @Service implements port)
    ↓
application/port/out/           (Output Port — Java interface)
    ↓
infrastructure/persistence/adapter/  (Persistence Adapter — JPA)
    ↓
PostgreSQL
```

## Project Structure

```
taskflow-angular/
├── src/
│   ├── api/          # Spring Boot Maven project
│   ├── db/           # Seeds
│   └── frontend/     # Angular project
├── tests/
├── docker-compose.yml
├── Dockerfile.api
└── Dockerfile.frontend
```

## Development

### Prerequisites
- Java 21
- Node 20+
- Docker + Docker Compose

### Run Everything (Docker)
```bash
docker compose up
```
- Frontend: http://localhost:4200
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- PostgreSQL: localhost:5434

### Backend Only
```bash
cd src/api
./mvnw spring-boot:run
```

### Frontend Only
```bash
cd src/frontend
npm install
npm start
```

## API Conventions

All endpoints follow the same envelope as `taskflow`:
```json
{
  "data": <payload>,
  "error": null,
  "meta": { "page": 0, "size": 20, "total": 100 }
}
```

### Endpoints
```
GET    /api/v1/health
GET    /api/v1/projects?page=0&size=20
GET    /api/v1/projects/{id}
POST   /api/v1/projects
PATCH  /api/v1/projects/{id}
DELETE /api/v1/projects/{id}
GET    /api/v1/tasks?page=0&size=20&projectId=&status=&priority=&search=
GET    /api/v1/tasks/{id}
POST   /api/v1/tasks
PATCH  /api/v1/tasks/{id}
DELETE /api/v1/tasks/{id}
```

## Testing

```bash
# Backend
cd src/api && ./mvnw test

# Frontend
cd src/frontend && npm test
```
