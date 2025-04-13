# Sanlam Bank Withdrawal System

A Spring Boot-based service to manage bank account withdrawals, event publishing, and withdrawal history. Built for high maintainability, observability, and testability.

## 🚀 Features

- ✅ Account withdrawal endpoint with:
  - Balance checks
  - Input validation
  - Rate limiting (max 3 withdrawals/min/account)
  - Withdrawal history recording
  - SNS event publishing (mocked by default)
- ✅ Asynchronous and retryable event publishing
- ✅ In-memory H2 database with schema auto-init
- ✅ Graceful shutdown support
- ✅ OpenAPI integration (WIP)
- ✅ Dockerized for portability

## 📦 Tech Stack

- Java 17
- Spring Boot 3.4
- H2 (In-memory DB)
- AWS SDK v2 (SNS)
- Springdoc OpenAPI
- Docker (Podman compatible)

## ⚙️ Running Locally

```bash
./mvnw clean install
./mvnw spring-boot:run

## Docker Build

./mvnw clean package -DskipTests
docker build -t sanlam-app .
docker run -p 8080:8080 sanlam-app
