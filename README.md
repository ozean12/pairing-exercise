# Billie Shipment Notification Service

## Overview
This project is an implementation of a shipment notification service for the Billie financial platform. It enables merchants to notify Billie about the shipment of an order, allowing them to get paid promptly. The service ensures that the sum of shipments does not exceed the total order amount.

## Features Implemented
- Shipment Notification: Allows merchants to notify Billie when an order is shipped. The system handles partial shipments and ensures the total shipment amount aligns with the order amount.

## Technologies Used
- Java
- Spring Boot
- Maven
- JPA/Hibernate
- Docker

## Getting Started
To set up the project on your local machine:
1. Clone the repository.
2. Navigate to the project directory.
3. Run mvn clean install to build the project.
4. Use docker-compose up to start the service.

## Testing
To run the integration tests:
1. Navigate to the project directory.
2. Execute mvn test.

## Future Enhancements
- Expand API functionality to cover more complex business scenarios.
- Enhance error handling for greater specificity in different business rule violations.
- Improve logging for better traceability and debugging.
- Optimize performance for handling large volumes of data.

---