"""Project business logic."""

from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession

from models.models import Project, Task
from schemas.project import ProjectCreate, ProjectUpdate


class ProjectService:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def list_projects(self, page: int = 1, per_page: int = 20) -> tuple[list[dict], int]:
        offset = (page - 1) * per_page

        # Get projects with task counts
        stmt = (
            select(Project, func.count(Task.id).label("task_count"))
            .outerjoin(Task, Task.project_id == Project.id)
            .group_by(Project.id)
            .order_by(Project.created_at.desc())
            .offset(offset)
            .limit(per_page)
        )
        result = await self.db.execute(stmt)
        rows = result.all()

        count_stmt = select(func.count(Project.id))
        total = (await self.db.execute(count_stmt)).scalar() or 0

        projects = []
        for project, task_count in rows:
            p = project.__dict__.copy()
            p["task_count"] = task_count
            projects.append(p)

        return projects, total

    async def get_project(self, project_id: int) -> Project | None:
        return await self.db.get(Project, project_id)

    async def create_project(self, data: ProjectCreate) -> Project:
        project = Project(**data.model_dump())
        self.db.add(project)
        await self.db.flush()
        await self.db.refresh(project)
        return project

    async def update_project(self, project_id: int, data: ProjectUpdate) -> Project | None:
        project = await self.db.get(Project, project_id)
        if not project:
            return None
        for key, value in data.model_dump(exclude_unset=True).items():
            setattr(project, key, value)
        await self.db.flush()
        await self.db.refresh(project)
        return project

    async def delete_project(self, project_id: int) -> bool:
        project = await self.db.get(Project, project_id)
        if not project:
            return False
        await self.db.delete(project)
        await self.db.flush()
        return True
