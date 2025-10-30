Employee Management System (EMS)

The Employee Management System (EMS) is a microservices-based platform built with Spring Boot, Spring Cloud, Kafka, and PostgreSQL. It demonstrates distributed communication, centralized configuration, authentication via JWT, and event-driven architecture using Kafka.

System Components

discovery-service: Eureka server for service registration (port 8761)

config-server: Centralized configuration management (port 8888)

gateway-service: API gateway handling routing and JWT validation (port 8080)

auth-service: User registration, login, and token management (port 8081)

employee-service: Manages employee and department data (port 8082)

kafka / zookeeper: Event streaming and message coordination (ports 9092 / 2181)

postgres-auth: Database for Auth Service (port 5432)

postgres-employee: Database for Employee Service (port 5433)

Setup Instructions

Requirements: Docker Desktop, Java 17+, Maven 3.9+, Git, Postman

Clone the repository
git clone https://github.com/theseven7h/employee-management-system.git
cd employee-management-system

Build all microservices
mvn clean package -DskipTests

Start containers
docker-compose up -d --build

Verify services
docker ps

Access:

Discovery: http://localhost:8761

Config Server: http://localhost:8888/actuator/health

Gateway: http://localhost:8080

API Endpoints
Auth Service
Endpoint	Method	Description
/api/v1/auth/register	POST	Register new user
/api/v1/auth/login	POST	Login and get JWT tokens
/api/v1/auth/refresh-token	POST	Refresh access token

Sample Register Request:

{
  "email": "john.doe@company.com",
  "password": "Password123",
  "firstName": "John",
  "lastName": "Doe"
}


Sample Response:

{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "john.doe@company.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["ROLE_EMPLOYEE"]
  }
}

Employee Service
Endpoint	Method	Description
/api/v1/employees	GET	Get all employees
/api/v1/employees/{id}	GET	Get employee by ID
/api/v1/employees	POST	Add new employee
/api/v1/employees/{id}	PUT	Update employee
/api/v1/employees/{id}	DELETE	Delete employee

Sample Employee Response:

{
  "employeeId": 1,
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@company.com",
  "department": "Engineering"
}

Architecture Decisions

Microservices architecture ensures independent deployment and scaling.

Spring Cloud Config centralizes configuration management.

Eureka enables service discovery.

Spring Cloud Gateway handles routing and authentication.

Kafka provides asynchronous event handling between services.

PostgreSQL databases are isolated per service for data integrity.

Docker Compose manages service orchestration for local deployment.

Assumptions

All services run within a shared Docker network.

Kafka topic user-events exists or is auto-created.

Default roles are seeded via database migration scripts.

JWT secret and expiration values are configured in the Config Server.

CI/CD Pipeline

The .github/workflows/ci-cd.yml workflow handles build, test, Docker image creation, and optional deployment.
To run locally:
mvn clean verify

Postman Collection

A Postman collection (postman/ems-collection.json) is included for testing all endpoints.

Author
James Tauri
GitHub: https://github.com/theseven7h

Repository: https://github.com/theseven7h/employee-management-system
