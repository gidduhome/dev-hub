---
name: docker-deploy
description: Docker and deployment patterns for TaskFlow
allowed tools: Read, Bash
---

# Docker

```bash
docker compose up -d            # Start
docker compose logs -f api      # Tail API logs
docker compose down -v          # Stop + remove volumes
```

## Services
- `api` → FastAPI on :8000
- `frontend` → Vite dev on :5173
- `db` → PostgreSQL on :5433 (host) / :5432 (container)

## Environment
- `.env.example` → committed template
- `.env` → real secrets (gitignored)
