# Patient Management Platform

Patient Management is a Java/Spring Boot microservices project that manages patient records, creates billing accounts over gRPC, and publishes patient-created events through Kafka.

The project separates patient and billing responsibilities into independently packaged services. It demonstrates when to use synchronous service-to-service communication for an immediate dependency and asynchronous events for downstream processing.

## Architecture

```text
Client
  |
  v
Patient Service (REST + persistence)
  |                         |
  | gRPC                    | Kafka event
  v                         v
Billing Service         Downstream consumers
```

## Engineering highlights

- REST endpoints for creating, reading, updating, and deleting patient records
- Request validation, duplicate-email handling, and centralized exception responses
- Synchronous gRPC contract between the patient and billing services
- Kafka publication of serialized patient events
- Spring Data persistence with local seed data
- Multi-stage Dockerfiles for both services
- Spring Boot test foundations and repeatable HTTP/gRPC request examples

## Technology

- Java and Spring Boot
- Spring Data JPA
- gRPC and Protocol Buffers
- Apache Kafka
- Docker
- Maven

## Repository layout

| Path | Purpose |
| --- | --- |
| `patient-service/` | Patient REST API, persistence, gRPC client, and Kafka producer |
| `billing-service/` | Billing-account gRPC service |
| `api-requests/` | Example patient REST requests |
| `grpc-requests/` | Example billing gRPC request |

## Run locally

Each service includes the Maven wrapper and its own application configuration.

```bash
cd billing-service
./mvnw spring-boot:run
```

In a second terminal:

```bash
cd patient-service
./mvnw spring-boot:run
```

The billing service listens for gRPC requests on port `9001`. Review each service's `application.properties` before running to configure its database, Kafka broker, and service address for your environment.
