---
name: testing
description: Testing patterns for TaskFlow projects (Pytest, Vitest, JUnit, Jasmine)
---

# Testing Patterns

Use **Arrange → Act → Assert** everywhere.

---

## TaskFlow (FastAPI + React)

### Pytest (Backend)
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

### Vitest (Frontend)
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

---

## TaskFlow Angular (Spring Boot + Angular)

### JUnit 5 + Testcontainers (Backend)
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("taskflow_test")
                    .withUsername("taskflow")
                    .withPassword("taskflow");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @Order(1)
    void createProject_validRequest_returns201() {
        // Arrange
        String body = """
                { "name": "Test Project", "description": "desc" }
                """;
        // Act
        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/projects"),
                new HttpEntity<>(body, jsonHeaders()), String.class);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
```
- Real PostgreSQL via Testcontainers (Docker must be running)
- `TestRestTemplate` for HTTP assertions
- Ordered tests for CRUD lifecycle flows
- Run with: `cd src/api && ./mvnw test`

### Jasmine + Karma (Frontend)
```typescript
describe('ProjectService', () => {
  let service: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ProjectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
```
- Co-locate tests: `component.spec.ts` beside `component.ts`
- Use `HttpClientTestingModule` to mock HTTP
- Run with: `cd src/frontend && npm test`
