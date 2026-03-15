"""Project endpoints."""

import logging

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.responses import JSONResponse
from sqlalchemy.ext.asyncio import AsyncSession

logger = logging.getLogger(__name__)

from models.database import get_db
from schemas.project import ProjectCreate, ProjectResponse, ProjectUpdate, ProjectWithTaskCount
from schemas.response import ok, paginated
from services.project_service import ProjectService

router = APIRouter()


@router.get("")
async def list_projects(page: int = 1, per_page: int = 20, db: AsyncSession = Depends(get_db)):
    service = ProjectService(db)
    projects, total = await service.list_projects(page=page, per_page=per_page)
    return paginated(
        [ProjectWithTaskCount.model_validate(p) for p in projects],
        page, per_page, total,
    )


@router.get("/{project_id}")
async def get_project(project_id: int, db: AsyncSession = Depends(get_db)):
    service = ProjectService(db)
    project = await service.get_project(project_id)
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")
    return ok(ProjectResponse.model_validate(project))


@router.post("", status_code=status.HTTP_201_CREATED)
async def create_project(payload: ProjectCreate, db: AsyncSession = Depends(get_db)):
    try:
        service = ProjectService(db)
        project = await service.create_project(payload)
        return ok(ProjectResponse.model_validate(project))
    except Exception as exc:
        logger.error("Failed to create project: %r", exc)
        return JSONResponse(
            status_code=500,
            content={"data": None, "error": str(exc) or "Failed to create project", "meta": {}},
        )


@router.patch("/{project_id}")
async def update_project(project_id: int, payload: ProjectUpdate, db: AsyncSession = Depends(get_db)):
    service = ProjectService(db)
    project = await service.update_project(project_id, payload)
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")
    return ok(ProjectResponse.model_validate(project))


@router.delete("/{project_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_project(project_id: int, db: AsyncSession = Depends(get_db)):
    service = ProjectService(db)
    if not await service.delete_project(project_id):
        raise HTTPException(status_code=404, detail="Project not found")
