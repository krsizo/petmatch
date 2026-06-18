# PetMatch API

PetMatch is a backend application for pet adoption and matching. The platform allows users to browse pets, manage adoption bookings, save favorites, receive personalized pet recommendations, and track booking events.

## Features

### Pet Management

* Create, update and delete pets
* Search pets by multiple criteria
* Find nearby pets by location
* Pagination and filtering support

### Booking Management

* Create adoption appointments
* Confirm and cancel bookings
* Mark pets as adopted
* Validate booking status transitions
* Prevent duplicate bookings for the same pet and time slot

### Matching System

* User preference management
* Match scoring algorithm
* Personalized pet recommendations
* Distance-based matching
* Budget, age, gender, size and temperament scoring

### Security

* JWT authentication
* Role-based authorization
* Argon2 password hashing
* Protected admin endpoints

### Events

* RabbitMQ integration
* Booking event publishing
* Event listener
* Event history tracking

## Technology Stack

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* Flyway
* RabbitMQ
* Docker
* Docker Compose
* JWT Authentication
* Swagger / OpenAPI
* JUnit 5
* Mockito
* MockMvc
* Testcontainers
* JaCoCo
* GitHub Actions

## Architecture

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
```

Event flow:

```text
BookingService
  ↓
RabbitMQ
  ↓
Booking Event Listener
  ↓
Booking Event History
```

Database schema changes are managed through Flyway migrations.

## Running Locally

### Prerequisites

* Java 17
* Docker Desktop

### Start application with Docker Compose

```bash
docker compose up --build
```

This starts:

* PetMatch API
* PostgreSQL
* RabbitMQ

### Start infrastructure only

```bash
docker compose up -d postgres rabbitmq
```

### Run application locally

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### Run tests

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
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

## Database Migrations

Database schema is managed using Flyway.

Migration scripts are located in:

```text
src/main/resources/db/migration
```

Flyway automatically applies pending migrations on application startup.

Example:

```text
V1__init.sql
```

Hibernate is configured to validate the schema instead of creating it automatically.

## Testing

### Unit Tests

* BookingStatusTransitionServiceTest
* PetScoringServiceTest
* MatchServiceTest

### Integration Tests

* BookingServiceIntegrationTest
* PetRepositoryIntegrationTest

Integration tests run against a real PostgreSQL container using Testcontainers.

### API Tests

* AuthControllerApiTest
* PetControllerApiTest
* BookingControllerSecurityTest

API tests are implemented using MockMvc.

## Code Coverage

Code coverage reports are generated using JaCoCo.

Generate coverage report:

```bash
./mvnw test
```

Report location:

```text
target/site/jacoco/index.html
```

## CI

GitHub Actions automatically runs the test suite on every push and pull request.

## Project Highlights

* Full Spring Boot backend application
* JWT-based security
* Real PostgreSQL integration tests with Testcontainers
* RabbitMQ event-driven booking flow
* Flyway database migrations
* Dockerized local environment
* Automated CI pipeline
* JaCoCo test coverage reports
* Swagger/OpenAPI documentation

## Future Improvements

* OpenAPI-first development
* Cloud deployment
* Frontend application
* WebSocket notifications
* AI-assisted pet matching
* Kubernetes deployment
