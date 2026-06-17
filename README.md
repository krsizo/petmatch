# PetMatch API

PetMatch is a backend application for pet adoption and matching. The platform allows users to browse pets, manage adoption bookings, receive personalized pet recommendations, and track adoption events.

## Features

### Pet Management

* Create, update and delete pets (admin only)
* Search pets by multiple criteria
* Find nearby pets by location
* Pagination and filtering support

### Booking Management

* Create adoption appointments
* Confirm and cancel bookings
* Mark pets as adopted
* Booking status transition validation

### Matching System

* User preferences
* Match scoring algorithm
* Personalized pet recommendations
* Distance-based matching

### Security

* JWT authentication
* Role-based authorization
* Argon2 password hashing

### Events

* RabbitMQ integration
* Booking event publishing
* Event history tracking

## Technology Stack

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* RabbitMQ
* Docker Compose
* JWT Authentication
* Swagger / OpenAPI
* JUnit 5
* Mockito
* Testcontainers
* GitHub Actions

## Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Additional integrations:

```text
Service
    ↓
RabbitMQ
    ↓
Booking Events
```

## Running Locally

### Prerequisites

* Java 17
* Docker Desktop

### Start infrastructure

```bash
docker compose up -d
```

### Run application

```bash
./mvnw spring-boot:run
```

### Run tests

```bash
./mvnw test
```

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

## Testing

### Unit Tests

* BookingStatusTransitionServiceTest
* PetScoringServiceTest
* MatchServiceTest

### Integration Tests

* BookingServiceIntegrationTest
* PetRepositoryIntegrationTest

### API Tests

* PetControllerApiTest
* BookingControllerSecurityTest

### Testcontainers

Integration tests run against a real PostgreSQL container.

## CI

GitHub Actions automatically runs all tests on every push and pull request.

## Future Improvements

* Flyway database migrations
* Dockerized application deployment
* Frontend application
* WebSocket notifications
* AI-assisted pet matching
* Kubernetes deployment
