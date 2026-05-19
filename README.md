# Booking System — Final Project
**Author: Beknazarov Miras**

## Project Overview
A full-featured Hotel Booking System backend built with Spring Boot.

## Tech Stack
- Java 17, Spring Boot 3.2
- Spring Security + JWT Authentication
- Spring Data JPA + PostgreSQL
- MapStruct, Lombok
- Swagger/OpenAPI
- Docker + Docker Compose (multistage build)

## Entities (6)
1. **User** — system users with roles
2. **Role** — ROLE_USER, ROLE_ADMIN
3. **Hotel** — hotels with city, stars, contact info
4. **Room** — rooms with type, capacity, price
5. **Booking** — bookings with dates, status, total price
6. **Review** — user reviews for hotels

## Architecture
```
Controller → Service → Repository → Database
```

## Features
- ✅ RESTful API (GET, POST, PUT, DELETE)
- ✅ JWT Authentication (register, login, refresh)
- ✅ Role-based Authorization (ADMIN / USER)
- ✅ Pagination, Sorting, Search, Filtering
- ✅ File Upload/Download (hotel & room images)
- ✅ Async Processing (@Async, CompletableFuture)
- ✅ DTO classes + manual mapping
- ✅ Bean Validation
- ✅ Global Exception Handler (@ControllerAdvice)
- ✅ Swagger UI documentation
- ✅ Request logging filter
- ✅ Dockerfile (multistage) + docker-compose
- ✅ Health checks

## Running with Docker
```bash
docker-compose up --build
```

## API Docs
After startup: http://localhost:8080/swagger-ui.html

## Running locally
```bash
# Start PostgreSQL first, then:
mvn spring-boot:run
```
# User and Role entities added
# Hotel and Room entities added
