# Billie Pair Programming

## Overview

This project is a Spring Boot application designed to manage shipments, orders, and organizations. It provides a RESTful API to handle various business operations such as creating shipments, updating order statuses, and retrieving order and shipment details.

## Problem Statement

The application aims to solve the problem of managing and tracking shipments and orders for an e-commerce platform. It allows merchants to notify the system of shipped orders to ensure timely payment processing and invoice management. The system also supports partial shipments and ensures that the sum of shipments does not exceed the total order amount.

## Proposed Solution

The solution involves building a set of RESTful endpoints that allow for the creation of shipments, updating order statuses, and querying orders and shipments. The system uses a PostgreSQL database to store data and Flyway for database migrations. The application is containerized using Docker, making it easy to deploy and run in different environments.

## Technical Stack

- *Spring Boot*: Framework for creating stand-alone, production-grade Spring-based applications.
- *Kotlin*: Programming language for writing concise and expressive code.
- *PostgreSQL*: Open-source relational database for data persistence.
- *Flyway*: Tool for version control and migration of the database schema.
- *Docker*: Platform for developing, shipping, and running applications inside containers.
- *Testcontainers*: Java library that supports JUnit tests with database instances provided by Docker containers.
- *JUnit 5*: The next generation of JUnit for writing tests and extensions.
- *MockK*: Kotlin library for mocking dependencies in tests.
- *Traefik*: Modern HTTP reverse proxy and load balancer.

## Running Tests

To run the integration and unit tests for the application, use the following command:

```shell
./gradlew test
```

The tests are configured to use Testcontainers, which will automatically start a PostgreSQL container for the duration of the tests.
## Running the Application
To run the application using Docker Compose, which will set up the application along with the PostgreSQL database and apply Flyway migrations, follow these steps:

1. Build the Jar for the application:
```shell
./gradlew clean build -x test
```
2. Start the services using Docker Compose:
```shell
docker-compose up --build -d
```

This command will start the PostgreSQL database, apply Flyway migrations, and run the application. The application will be accessible via the configured ports and routes.
## API Documentation
The API documentation is available at http://billie.localhost/swagger-ui.html when the application is running. It provides an interactive UI to explore and test the available endpoints.
