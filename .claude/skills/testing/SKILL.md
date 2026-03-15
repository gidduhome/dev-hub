---
name: testing-patterns
description: Pytest and Vitest testing patterns for TaskFlow
allowed tools: Read, Grep, Glob
---

# Testing Patterns

Use **Arrange → Act → Assert** everywhere.

## Pytest (Backend)
```python
async def test_create_task(async_client, db_session):
    # Arrange
    payload = {"title": "Ship feature", "project_id": 1}
    # Act
    response = await async_client.post("/api/v1/tasks", json=payload)
    # Assert
    assert response.status_code == 201
    assert response.json()["data"]["title"] == "Ship feature"
```
- Use factory fixtures in `conftest.py`
- `pytest.mark.asyncio` for async tests
- Mock external services, never call real ones

## Vitest (Frontend)
```typescript
describe('TaskCard', () => {
  it('shows task title', () => {
    const task = createMockTask({ title: 'Review PR' });
    render(<TaskCard task={task} />);
    expect(screen.getByText('Review PR')).toBeInTheDocument();
  });
});
```
- Co-locate tests: `Component.test.tsx` beside `Component.tsx`
- Mock API with `msw`
