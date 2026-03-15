# Project: TaskFlow

A task and project management application.

## Tech Stack

- **Backend**: Python 3.12 + FastAPI + SQLAlchemy (async) + Alembic
- **Frontend**: React 18 + TypeScript + Vite + TailwindCSS
- **Database**: PostgreSQL 16
- **Containerization**: Docker + Docker Compose
- **Testing**: Pytest (API), Vitest + React Testing Library (Frontend)

## Commands

```bash
# Start everything
docker compose up -d

# Backend only
cd src/api && uvicorn main:app --reload --port 8000

# Frontend only
cd src/frontend && npm run dev

# Database migrations
alembic upgrade head
alembic revision --autogenerate -m "description"

# Tests
pytest tests/ -v
cd src/frontend && npm run test

# Lint
ruff check src/api/
cd src/frontend && npm run lint
```

## Architecture

- `/src/api` → FastAPI REST API (routes, models, schemas, services)
- `/src/frontend` → React SPA (Vite + TypeScript + TailwindCSS)
- `/src/db` → Alembic migrations and seed data
- `/tests` → Pytest and Vitest test suites

## Directory Map

- `src/api/main.py` → App entry point, middleware, router mount
- `src/api/routes/` → Thin endpoint handlers
- `src/api/models/` → SQLAlchemy ORM models
- `src/api/schemas/` → Pydantic request/response validation
- `src/api/services/` → Business logic (repository pattern)
- `src/api/middleware/` → Auth, CORS, error handling
- `src/db/migrations/` → Alembic version files
- `src/frontend/src/pages/` → Route-level page components
- `src/frontend/src/components/` → Reusable UI components
- `src/frontend/src/services/` → API client layer
- `src/frontend/src/hooks/` → Custom React hooks

## Design Decisions

- Repository pattern: services own data access, routes stay thin
- Consistent API envelope: `{ "data": ..., "error": null, "meta": {} }`
- Every DB table has `id`, `created_at`, `updated_at` columns
- Frontend uses React Query for server state
- Vite proxies `/api/*` to backend in dev mode
- Environment config via `.env` — never commit secrets

## Workflows

- Run `pytest` before committing backend changes
- Run `npm run lint && npm run test` before committing frontend changes
- DB changes require Alembic migrations — never modify DB directly
- Feature branches → merge to `main` via PR
- Start a new Claude Code session per feature

## Gotchas

- PostgreSQL: port 5432 in Docker, 5433 on host (avoids conflicts)
- SQLAlchemy async sessions require `async with` — always close them
- Alembic env.py must import all models for autogenerate detection
- React Query queryKey arrays must be stable references
- TailwindCSS classes won't work if not in the `content` paths in config
