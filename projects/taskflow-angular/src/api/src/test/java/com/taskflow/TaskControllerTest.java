package com.taskflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@code /api/v1/tasks}.
 *
 * <p>Starts a real Spring Boot application wired to a Testcontainers PostgreSQL instance.
 * A project is created first (in {@link #setup_createProject()}) so tasks can reference it.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerTest {

    // ---- Testcontainers PostgreSQL ------------------------------------------

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

    // ---- Shared state -------------------------------------------------------

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long projectId;
    private static Long taskId;

    // ---- Helpers ------------------------------------------------------------

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private JsonNode parseBody(ResponseEntity<String> response) {
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response: " + response.getBody(), e);
        }
    }

    // ---- Tests --------------------------------------------------------------

    @Test
    @Order(1)
    void setup_createProject() {
        String body = """
                { "name": "Task Test Project", "description": "Project for task tests" }
                """;

        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/projects"),
                new HttpEntity<>(body, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JsonNode parsed = parseBody(response);
        projectId = parsed.at("/data/id").asLong();
        assertThat(projectId).isPositive();
    }

    @Test
    @Order(2)
    void listTasks_empty_returns200() {
        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/tasks"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data").isArray()).isTrue();
    }

    @Test
    @Order(3)
    void createTask_validRequest_returns201() {
        assertThat(projectId).isNotNull();

        String body = """
                {
                  "title":     "Implement login",
                  "description": "Add JWT-based login flow",
                  "status":    "TODO",
                  "priority":  "HIGH",
                  "projectId": %d
                }
                """.formatted(projectId);

        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/tasks"),
                new HttpEntity<>(body, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JsonNode parsed = parseBody(response);
        assertThat(parsed.at("/data/title").asText()).isEqualTo("Implement login");
        assertThat(parsed.at("/data/status").asText()).isEqualTo("TODO");
        assertThat(parsed.at("/data/priority").asText()).isEqualTo("HIGH");

        taskId = parsed.at("/data/id").asLong();
        assertThat(taskId).isPositive();
    }

    @Test
    @Order(4)
    void getTask_existingId_returns200() {
        assertThat(taskId).isNotNull();

        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/tasks/" + taskId), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data/id").asLong()).isEqualTo(taskId);
        assertThat(body.at("/data/title").asText()).isEqualTo("Implement login");
    }

    @Test
    @Order(5)
    void listTasks_filterByProjectId_returns200() {
        assertThat(projectId).isNotNull();

        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/tasks?projectId=" + projectId), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data").isArray()).isTrue();
        assertThat(body.at("/meta/total").asLong()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Order(6)
    void listTasks_filterByStatus_returns200() {
        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/tasks?status=TODO"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        body.at("/data").forEach(task ->
                assertThat(task.at("/status").asText()).isEqualTo("TODO"));
    }

    @Test
    @Order(7)
    void listTasks_filterBySearch_returns200() {
        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/tasks?search=login"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data").isArray()).isTrue();
        assertThat(body.at("/meta/total").asLong()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Order(8)
    void updateTask_changeStatus_returns200() {
        assertThat(taskId).isNotNull();

        String body = """
                { "status": "IN_PROGRESS" }
                """;

        ResponseEntity<String> response = rest.exchange(
                url("/api/v1/tasks/" + taskId),
                HttpMethod.PATCH,
                new HttpEntity<>(body, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode parsed = parseBody(response);
        assertThat(parsed.at("/data/status").asText()).isEqualTo("IN_PROGRESS");
    }

    @Test
    @Order(9)
    void createTask_blankTitle_returns400() {
        assertThat(projectId).isNotNull();

        String body = """
                { "title": "", "projectId": %d }
                """.formatted(projectId);

        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/tasks"),
                new HttpEntity<>(body, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(10)
    void createTask_nonExistentProject_returns404() {
        String body = """
                { "title": "Orphan task", "projectId": 99999 }
                """;

        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/tasks"),
                new HttpEntity<>(body, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(11)
    void deleteTask_existingId_returns204() {
        assertThat(taskId).isNotNull();

        ResponseEntity<Void> response = rest.exchange(
                url("/api/v1/tasks/" + taskId),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(12)
    void getTask_afterDeletion_returns404() {
        assertThat(taskId).isNotNull();

        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/tasks/" + taskId), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
