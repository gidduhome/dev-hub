---
name: code-review
description: Reviews code for quality, security, and project conventions
allowed tools: Read, Grep, Glob
---

# Code Review Checklist

## Security
- No hardcoded secrets or tokens
- SQL uses SQLAlchemy ORM (parameterized) — no raw string queries
- User input validated via Pydantic schemas before processing
- Protected endpoints check auth

## Backend
- Type hints on all function signatures
- Async functions use `async def` / `await` correctly
- DB sessions scoped with `async with`
- Business logic in `/services`, not route handlers
- Standard response envelope on all endpoints

## Frontend
- No `any` types
- useEffect dependency arrays are correct
- API calls go through service layer
- Components under 200 lines

## General
- No commented-out code
- No `console.log` / `print()` in production paths
- Tests exist for new functionality
