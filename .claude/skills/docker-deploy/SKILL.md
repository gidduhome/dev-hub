---
name: docker-deploy
description: Docker and deployment patterns for TaskFlow projects
---

# Docker

```bash
docker compose up -d            # Start
docker compose logs -f api      # Tail API logs
docker compose down -v          # Stop + remove volumes
```

## TaskFlow (FastAPI + React)
- `api` → FastAPI on :8000
- `frontend` → Vite dev on :5173
- `db` → PostgreSQL on :5433 (host) / :5432 (container)

## TaskFlow Angular (Spring Boot + Angular)
- `api` → Spring Boot on :8080
- `frontend` → Angular dev on :4200
- `db` → PostgreSQL on :5434 (host) / :5432 (container)

> Both projects use unique host ports and can run simultaneously.

## Environment
- `.env.example` → committed template
- `.env` → real secrets (gitignored)
