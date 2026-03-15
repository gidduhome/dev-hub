"""Task business logic."""

from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession

from models.models import Task, TaskStatus, TaskPriority
from schemas.task import TaskCreate, TaskUpdate


class TaskService:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def list_tasks(
        self,
        page: int = 1,
        per_page: int = 20,
        project_id: int | None = None,
        status: TaskStatus | None = None,
        priority: TaskPriority | None = None,
        search: str | None = None,
    ) -> tuple[list[Task], int]:
        offset = (page - 1) * per_page

        stmt = select(Task)
        count_stmt = select(func.count(Task.id))

        if project_id:
            stmt = stmt.where(Task.project_id == project_id)
            count_stmt = count_stmt.where(Task.project_id == project_id)
        if status:
            stmt = stmt.where(Task.status == status)
            count_stmt = count_stmt.where(Task.status == status)
        if priority:
            stmt = stmt.where(Task.priority == priority)
            count_stmt = count_stmt.where(Task.priority == priority)
        if search:
            stmt = stmt.where(Task.title.ilike(f"%{search}%"))
            count_stmt = count_stmt.where(Task.title.ilike(f"%{search}%"))

        stmt = stmt.order_by(Task.created_at.desc()).offset(offset).limit(per_page)

        result = await self.db.execute(stmt)
        tasks = list(result.scalars().all())
        total = (await self.db.execute(count_stmt)).scalar() or 0

        return tasks, total

    async def get_task(self, task_id: int) -> Task | None:
        return await self.db.get(Task, task_id)

    async def create_task(self, data: TaskCreate) -> Task:
        task = Task(**data.model_dump())
        self.db.add(task)
        await self.db.flush()
        await self.db.refresh(task)
        return task

    async def update_task(self, task_id: int, data: TaskUpdate) -> Task | None:
        task = await self.db.get(Task, task_id)
        if not task:
            return None
        for key, value in data.model_dump(exclude_unset=True).items():
            setattr(task, key, value)
        await self.db.flush()
        await self.db.refresh(task)
        return task

    async def delete_task(self, task_id: int) -> bool:
        task = await self.db.get(Task, task_id)
        if not task:
            return False
        await self.db.delete(task)
        await self.db.flush()
        return True
