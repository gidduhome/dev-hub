"""Task endpoints."""

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from models.database import get_db
from models.models import TaskStatus, TaskPriority
from schemas.task import TaskCreate, TaskResponse, TaskUpdate
from schemas.response import ok, paginated
from services.task_service import TaskService

router = APIRouter()


@router.get("")
async def list_tasks(
    page: int = 1,
    per_page: int = 20,
    project_id: int | None = None,
    status_filter: TaskStatus | None = None,
    priority: TaskPriority | None = None,
    search: str | None = None,
    db: AsyncSession = Depends(get_db),
):
    service = TaskService(db)
    tasks, total = await service.list_tasks(
        page=page, per_page=per_page,
        project_id=project_id, status=status_filter,
        priority=priority, search=search,
    )
    return paginated(
        [TaskResponse.model_validate(t) for t in tasks],
        page, per_page, total,
    )


@router.get("/{task_id}")
async def get_task(task_id: int, db: AsyncSession = Depends(get_db)):
    service = TaskService(db)
    task = await service.get_task(task_id)
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    return ok(TaskResponse.model_validate(task))


@router.post("", status_code=status.HTTP_201_CREATED)
async def create_task(payload: TaskCreate, db: AsyncSession = Depends(get_db)):
    service = TaskService(db)
    task = await service.create_task(payload)
    return ok(TaskResponse.model_validate(task))


@router.patch("/{task_id}")
async def update_task(task_id: int, payload: TaskUpdate, db: AsyncSession = Depends(get_db)):
    service = TaskService(db)
    task = await service.update_task(task_id, payload)
    if not task:
        raise HTTPException(status_code=404, detail="Task not found")
    return ok(TaskResponse.model_validate(task))


@router.delete("/{task_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_task(task_id: int, db: AsyncSession = Depends(get_db)):
    service = TaskService(db)
    if not await service.delete_task(task_id):
        raise HTTPException(status_code=404, detail="Task not found")
