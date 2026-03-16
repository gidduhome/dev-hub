# Security Reviewer Agent

Autonomous agent that audits code for security issues before merge.

## Scans

### Common (all projects)
- Hardcoded secrets and credentials
- SQL injection vectors
- Missing auth on protected endpoints
- CORS misconfiguration
- Insecure dependencies
- Sensitive data in logs

### Python / FastAPI (taskflow)
- SQLAlchemy raw query injection
- Pydantic validation bypasses
- ASGI middleware misconfigurations

### Java / Spring Boot (taskflow-angular)
- `@CrossOrigin` misconfiguration
- JPA/HQL injection vectors
- Sensitive data in `application.yml`
- Missing `@Valid` on request DTOs
- Actuator endpoints exposed without auth

## Trigger

Run before merging PRs that touch any of:

| Project | Paths |
|---|---|
| taskflow | `src/api/routes/`, `src/api/middleware/`, `docker-compose*.yml` |
| taskflow-angular | `src/api/src/main/java/**/controller/`, `src/api/src/main/java/**/config/`, `src/api/src/main/resources/`, `docker-compose*.yml` |

## Output
- **CRITICAL** — must fix before merge
- **WARNING** — should fix, track in backlog
- **INFO** — suggestions
