"""Tests for task CRUD endpoints."""

import pytest
from httpx import AsyncClient


async def _create_project(client: AsyncClient, name: str = "Test Project") -> int:
    """Helper to create a project and return its ID."""
    res = await client.post("/api/v1/projects", json={"name": name})
    return res.json()["data"]["id"]


@pytest.mark.asyncio
async def test_create_task(async_client: AsyncClient):
    # Arrange
    project_id = await _create_project(async_client)
    payload = {"title": "Write tests", "priority": "high", "project_id": project_id}

    # Act
    response = await async_client.post("/api/v1/tasks", json=payload)

    # Assert
    assert response.status_code == 201
    data = response.json()["data"]
    assert data["title"] == "Write tests"
    assert data["status"] == "todo"
    assert data["priority"] == "high"


@pytest.mark.asyncio
async def test_list_tasks_with_filter(async_client: AsyncClient):
    # Arrange
    project_id = await _create_project(async_client)
    await async_client.post("/api/v1/tasks", json={"title": "Task A", "project_id": project_id, "priority": "high"})
    await async_client.post("/api/v1/tasks", json={"title": "Task B", "project_id": project_id, "priority": "low"})

    # Act — filter by priority
    response = await async_client.get("/api/v1/tasks", params={"priority": "high"})

    # Assert
    assert response.status_code == 200
    tasks = response.json()["data"]
    assert all(t["priority"] == "high" for t in tasks)


@pytest.mark.asyncio
async def test_update_task_status(async_client: AsyncClient):
    # Arrange
    project_id = await _create_project(async_client)
    create_res = await async_client.post("/api/v1/tasks", json={"title": "Move me", "project_id": project_id})
    task_id = create_res.json()["data"]["id"]

    # Act
    response = await async_client.patch(f"/api/v1/tasks/{task_id}", json={"status": "in_progress"})

    # Assert
    assert response.status_code == 200
    assert response.json()["data"]["status"] == "in_progress"


@pytest.mark.asyncio
async def test_delete_task(async_client: AsyncClient):
    # Arrange
    project_id = await _create_project(async_client)
    create_res = await async_client.post("/api/v1/tasks", json={"title": "Remove me", "project_id": project_id})
    task_id = create_res.json()["data"]["id"]

    # Act
    response = await async_client.delete(f"/api/v1/tasks/{task_id}")

    # Assert
    assert response.status_code == 204


@pytest.mark.asyncio
async def test_search_tasks(async_client: AsyncClient):
    # Arrange
    project_id = await _create_project(async_client)
    await async_client.post("/api/v1/tasks", json={"title": "Deploy to production", "project_id": project_id})
    await async_client.post("/api/v1/tasks", json={"title": "Fix login bug", "project_id": project_id})

    # Act
    response = await async_client.get("/api/v1/tasks", params={"search": "deploy"})

    # Assert
    assert response.status_code == 200
    tasks = response.json()["data"]
    assert len(tasks) >= 1
    assert "deploy" in tasks[0]["title"].lower()
