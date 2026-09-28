# TaskFlow API

A clean, production-style **task management REST API** built with **Java 21** and **Spring Boot 3**.

![CI](https://github.com/<Abhishek-verma>/taskflow-api/actions/workflows/ci.yml/badge.svg)

## Features

- CRUD endpoints with pagination, sorting, and status filtering
- Layered architecture: controller → service → repository
- Bean Validation on request DTOs (Java records)
- RFC 7807 `ProblemDetail` error responses via a global exception handler
- OpenAPI / Swagger UI documentation
- Unit tests (Mockito) and web-layer tests (`@WebMvcTest`)
- Multi-stage Dockerfile and GitHub Actions CI

## Tech stack

Java 21 · Spring Boot 3.3 · Spring Data JPA · H2 (in-memory) · springdoc-openapi · JUnit 5 · Maven

## Getting started

**Prerequisites:** JDK 21+ and Maven 3.9+

```bash
mvn spring-boot:run
```

- API base URL: `http://localhost:8080/api/v1/tasks`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health: `http://localhost:8080/actuator/health`

### Run tests

```bash
mvn verify
```

### Run with Docker

```bash
docker build -t taskflow-api .
docker run -p 8080:8080 taskflow-api
```

## API overview

| Method | Endpoint             | Description                                |
|--------|----------------------|--------------------------------------------|
| POST   | `/api/v1/tasks`      | Create a task                              |
| GET    | `/api/v1/tasks`      | List tasks (`?status=TODO&page=0&size=20`) |
| GET    | `/api/v1/tasks/{id}` | Get a task                                 |
| PUT    | `/api/v1/tasks/{id}` | Update a task                              |
| DELETE | `/api/v1/tasks/{id}` | Delete a task                              |

### Example

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Write README","priority":"HIGH","dueDate":"2026-10-15"}'
```

## Project structure

```
src/main/java/com/example/taskflow
├── config/       OpenAPI configuration
├── controller/   REST controllers
├── dto/          Request/response records
├── exception/    Custom exceptions + global handler
├── model/        JPA entity and enums
├── repository/   Spring Data repositories
└── service/      Business logic
```

## Roadmap ideas

- Swap H2 for PostgreSQL + Flyway migrations
- Add Spring Security with JWT
- Add Testcontainers integration tests

## License

MIT
