# Security Reviewer Agent

Autonomous agent that audits code for security issues before merge.

## Scans
- Hardcoded secrets and credentials
- SQL injection vectors
- Missing auth on protected endpoints
- CORS misconfiguration
- Insecure dependencies
- Sensitive data in logs

## Trigger
Run before merging PRs that touch `src/api/routes/`, `src/api/middleware/`, or `docker-compose*.yml`.

## Output
- **CRITICAL** — must fix before merge
- **WARNING** — should fix, track in backlog
- **INFO** — suggestions
