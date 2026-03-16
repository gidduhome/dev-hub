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
 * Integration tests for {@code /api/v1/projects}.
 *
 * <p>Starts a real Spring Boot application wired to a Testcontainers PostgreSQL instance.
 * Tests run in a defined order so that the project created in one test is available in later tests.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectControllerTest {

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

    /** Holds the id of the project created during the POST test. */
    private static Long createdProjectId;

    // ---- Helpers -------------------------------------------------------------

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // ---- Tests ---------------------------------------------------------------

    @Test
    @Order(1)
    void listProjects_empty_returns200() {
        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/projects"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data").isArray()).isTrue();
        assertThat(body.at("/meta/total").asLong()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @Order(2)
    void createProject_validRequest_returns201() {
        String requestBody = """
                { "name": "My First Project", "description": "Integration test project" }
                """;

        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/projects"),
                new HttpEntity<>(requestBody, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data/name").asText()).isEqualTo("My First Project");
        assertThat(body.at("/data/id").asLong()).isPositive();

        createdProjectId = body.at("/data/id").asLong();
    }

    @Test
    @Order(3)
    void getProject_existingId_returns200() {
        assertThat(createdProjectId).isNotNull();

        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/projects/" + createdProjectId), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data/id").asLong()).isEqualTo(createdProjectId);
        assertThat(body.at("/data/name").asText()).isEqualTo("My First Project");
    }

    @Test
    @Order(4)
    void getProject_nonExistentId_returns404() {
        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/projects/99999"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        JsonNode body = parseBody(response);
        assertThat(body.at("/error").asText()).contains("not found");
    }

    @Test
    @Order(5)
    void updateProject_validPatch_returns200() {
        assertThat(createdProjectId).isNotNull();

        String requestBody = """
                { "name": "Updated Project Name" }
                """;

        ResponseEntity<String> response = rest.exchange(
                url("/api/v1/projects/" + createdProjectId),
                HttpMethod.PATCH,
                new HttpEntity<>(requestBody, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode body = parseBody(response);
        assertThat(body.at("/data/name").asText()).isEqualTo("Updated Project Name");
    }

    @Test
    @Order(6)
    void createProject_blankName_returns400() {
        String requestBody = """
                { "name": "   ", "description": "This should fail validation" }
                """;

        ResponseEntity<String> response = rest.postForEntity(
                url("/api/v1/projects"),
                new HttpEntity<>(requestBody, jsonHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(7)
    void deleteProject_existingId_returns204() {
        assertThat(createdProjectId).isNotNull();

        ResponseEntity<Void> response = rest.exchange(
                url("/api/v1/projects/" + createdProjectId),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(8)
    void getProject_afterDeletion_returns404() {
        assertThat(createdProjectId).isNotNull();

        ResponseEntity<String> response = rest.getForEntity(
                url("/api/v1/projects/" + createdProjectId), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ---- Utility -------------------------------------------------------------

    private JsonNode parseBody(ResponseEntity<String> response) {
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response body: " + response.getBody(), e);
        }
    }
}
