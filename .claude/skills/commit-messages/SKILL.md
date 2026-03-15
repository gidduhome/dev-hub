---
name: commit-messages
description: Conventional commit format for TaskFlow
allowed tools: Read, Bash
---

# Commits

Format: `<type>(<scope>): <description>`

Types: feat, fix, refactor, test, docs, chore, perf
Scopes: api, ui, db, infra

Examples:
```
feat(api): add task status transitions
fix(ui): prevent double-click on create button
test(api): integration tests for project CRUD
chore(infra): add health check to docker compose
```

Rules: ≤72 char subject, imperative mood, no trailing period.
