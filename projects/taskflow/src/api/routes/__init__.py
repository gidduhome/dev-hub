"""API route aggregator."""

from fastapi import APIRouter

from routes.projects import router as projects_router
from routes.tasks import router as tasks_router

api_router = APIRouter()

api_router.include_router(projects_router, prefix="/projects", tags=["Projects"])
api_router.include_router(tasks_router, prefix="/tasks", tags=["Tasks"])
