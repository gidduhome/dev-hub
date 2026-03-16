# Projects Monorepo

A single git repository housing multiple independent projects across different tech stacks.

## Projects

| Project | Stack | Location |
|---------|-------|----------|
| [TaskFlow](projects/taskflow/) | FastAPI + React + PostgreSQL | `projects/taskflow/` |
| [TaskFlow Angular](projects/taskflow-angular/) | Spring Boot + Angular + PostgreSQL | `projects/taskflow-angular/` |

---

## Adding a New Project

1. Create a folder: `projects/<project-name>/`
2. Add its own `CLAUDE.md`, `docker-compose.yml`, `.env.example`, etc.
3. Each project is fully self-contained — no shared dependencies between projects

---

## Repo Structure

```
.
├── .claude/settings.json   ← shared Claude Code config + security hook
├── scripts/sec.sh          ← pre-commit secret scanner (shared)
├── agents/                 ← autonomous agent definitions
├── commands/               ← reusable workflow commands
├── .gitignore              ← multi-stack ignores
└── projects/
    ├── taskflow/           ← TaskFlow app (FastAPI + React + PostgreSQL)
    └── taskflow-angular/   ← TaskFlow app (Spring Boot + Angular + PostgreSQL)
```

---

## TaskFlow Quick Start

```bash
cd projects/taskflow

# Copy env config
cp .env.example .env

# Start all services
docker compose up -d

# Run migrations
alembic upgrade head

# App:  http://localhost:5173
# API:  http://localhost:8000/api/v1/docs
```

## TaskFlow Angular Quick Start

```bash
cd projects/taskflow-angular

# Copy env config
cp .env.example .env

# Start all services
docker compose up -d

# App:        http://localhost:4200
# API:        http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```
