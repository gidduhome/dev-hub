"""Tests for project CRUD endpoints."""

import pytest
from httpx import AsyncClient


@pytest.mark.asyncio
async def test_create_project(async_client: AsyncClient):
    # Arrange
    payload = {"name": "Test Project", "description": "A test project"}

    # Act
    response = await async_client.post("/api/v1/projects", json=payload)

    # Assert
    assert response.status_code == 201
    data = response.json()["data"]
    assert data["name"] == "Test Project"
    assert data["id"] is not None


@pytest.mark.asyncio
async def test_list_projects(async_client: AsyncClient):
    # Arrange
    await async_client.post("/api/v1/projects", json={"name": "Project A"})
    await async_client.post("/api/v1/projects", json={"name": "Project B"})

    # Act
    response = await async_client.get("/api/v1/projects")

    # Assert
    assert response.status_code == 200
    assert len(response.json()["data"]) >= 2


@pytest.mark.asyncio
async def test_get_project(async_client: AsyncClient):
    # Arrange
    create_res = await async_client.post("/api/v1/projects", json={"name": "Fetch Me"})
    project_id = create_res.json()["data"]["id"]

    # Act
    response = await async_client.get(f"/api/v1/projects/{project_id}")

    # Assert
    assert response.status_code == 200
    assert response.json()["data"]["name"] == "Fetch Me"


@pytest.mark.asyncio
async def test_get_project_not_found(async_client: AsyncClient):
    response = await async_client.get("/api/v1/projects/99999")
    assert response.status_code == 404


@pytest.mark.asyncio
async def test_update_project(async_client: AsyncClient):
    # Arrange
    create_res = await async_client.post("/api/v1/projects", json={"name": "Old Name"})
    project_id = create_res.json()["data"]["id"]

    # Act
    response = await async_client.patch(f"/api/v1/projects/{project_id}", json={"name": "New Name"})

    # Assert
    assert response.status_code == 200
    assert response.json()["data"]["name"] == "New Name"


@pytest.mark.asyncio
async def test_delete_project(async_client: AsyncClient):
    # Arrange
    create_res = await async_client.post("/api/v1/projects", json={"name": "Delete Me"})
    project_id = create_res.json()["data"]["id"]

    # Act
    response = await async_client.delete(f"/api/v1/projects/{project_id}")

    # Assert
    assert response.status_code == 204

    # Verify gone
    get_res = await async_client.get(f"/api/v1/projects/{project_id}")
    assert get_res.status_code == 404
