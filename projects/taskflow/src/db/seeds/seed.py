"""Seed the database with sample projects and tasks."""

import asyncio
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "api"))

from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession, async_sessionmaker
from models.database import Base
from models.models import Project, Task, TaskStatus, TaskPriority

DATABASE_URL = "postgresql+asyncpg://taskflow:taskflow@localhost:5433/taskflow"


async def seed():
    engine = create_async_engine(DATABASE_URL)

    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

    session_factory = async_sessionmaker(engine, class_=AsyncSession, expire_on_commit=False)

    async with session_factory() as session:
        # Projects
        p1 = Project(name="Website Redesign", description="Overhaul the company website with new branding")
        p2 = Project(name="Mobile App", description="Build iOS and Android app for customers")
        p3 = Project(name="API Migration", description="Migrate legacy REST API to FastAPI")
        session.add_all([p1, p2, p3])
        await session.flush()

        # Tasks
        tasks = [
            Task(title="Design new homepage mockup", status=TaskStatus.IN_PROGRESS, priority=TaskPriority.HIGH, project_id=p1.id),
            Task(title="Set up TailwindCSS config", status=TaskStatus.DONE, priority=TaskPriority.MEDIUM, project_id=p1.id),
            Task(title="Implement responsive navigation", status=TaskStatus.TODO, priority=TaskPriority.HIGH, project_id=p1.id),
            Task(title="Write copy for About page", status=TaskStatus.TODO, priority=TaskPriority.LOW, project_id=p1.id),

            Task(title="Set up React Native project", status=TaskStatus.DONE, priority=TaskPriority.HIGH, project_id=p2.id),
            Task(title="Implement auth flow", status=TaskStatus.IN_PROGRESS, priority=TaskPriority.HIGH, project_id=p2.id),
            Task(title="Build task list screen", status=TaskStatus.TODO, priority=TaskPriority.MEDIUM, project_id=p2.id),
            Task(title="Push notification integration", status=TaskStatus.TODO, priority=TaskPriority.LOW, project_id=p2.id),

            Task(title="Audit existing endpoints", status=TaskStatus.DONE, priority=TaskPriority.HIGH, project_id=p3.id),
            Task(title="Write Pydantic schemas", status=TaskStatus.IN_PROGRESS, priority=TaskPriority.HIGH, project_id=p3.id),
            Task(title="Set up Alembic migrations", status=TaskStatus.DONE, priority=TaskPriority.MEDIUM, project_id=p3.id),
            Task(title="Load testing", status=TaskStatus.TODO, priority=TaskPriority.MEDIUM, project_id=p3.id),
        ]
        session.add_all(tasks)
        await session.commit()

    await engine.dispose()
    print("✅ Seeded 3 projects and 12 tasks.")


if __name__ == "__main__":
    asyncio.run(seed())
