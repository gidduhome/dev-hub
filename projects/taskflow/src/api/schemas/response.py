"""Standard API response envelope."""

from typing import Any

from pydantic import BaseModel


class Meta(BaseModel):
    page: int = 1
    per_page: int = 20
    total: int = 0


class ApiResponse(BaseModel):
    data: Any = None
    error: str | None = None
    meta: Meta = Meta()


def ok(data: Any, meta: dict | None = None) -> dict:
    """Success response."""
    return {"data": data, "error": None, "meta": meta or {}}


def paginated(data: Any, page: int, per_page: int, total: int) -> dict:
    """Paginated success response."""
    return {
        "data": data,
        "error": None,
        "meta": {"page": page, "per_page": per_page, "total": total},
    }


def error(message: str, status: int = 400) -> dict:
    """Error response."""
    return {"data": None, "error": message, "meta": {}}
