---
name: api-design
description: REST API design conventions for TaskFlow endpoints
allowed tools: Read, Grep, Glob
---

# API Design

## URLs
- Plural nouns: `/api/v1/tasks`, `/api/v1/projects`
- Nested: `/api/v1/projects/{id}/tasks`
- Versioned prefix: `/api/v1/`

## Response Envelope
```json
{ "data": {}, "error": null, "meta": { "page": 1, "total": 42, "per_page": 20 } }
```

## Status Codes
200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict, 422 Unprocessable, 500 Server Error

## Pagination
`?page=1&per_page=20&sort=created_at&order=desc`

## Filtering
`?status=in_progress&priority=high&search=deploy`
