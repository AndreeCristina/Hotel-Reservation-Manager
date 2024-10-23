# Business Domain: Hotel Reservation Manager

The Hotel Reservation Manager is a Spring Boot-based application designed to manage hotel reservations, rooms, and guest details. The application exposes a set of RESTful APIs for creating, reading, updating, and deleting (CRUD) reservations and room availability. The project also includes robust validation, error handling, and testing using JUnit and Mockito.

## Tech stack
Java 17: Programming language.
Spring Boot: Backend framework.
Maven: Dependency management and project build tool.
PostgreSQL: Primary database for data persistence.
H2 in-memory database: Used for integration testing.
MockMVC: For integration tests to simulate HTTP requests.
JUnit: Unit testing framework.
Mockito: Mocking framework for unit tests.
Hibernate: ORM for managing database entities.

## Features

### Reservation Management: 
Create, update, delete, and fetch reservations.

### Room Availability: 
Fetch available rooms based on user queries.

### Custom Queries: 
An API endpoint for retrieving reservations based on multiple parameters like check-in date, check-out date, and room type.

### Validation: 
DTO validation and service-layer validation to ensure data integrity.

### Error Handling:
Custom exceptions with meaningful HTTP status codes and error messages.

### Testing:
Unit and integration tests covering all critical components.

### Database Population: 
Supports database seeding through SQL scripts or POST API.

## REST API Endpoints

### Reservation APIs

POST /api/reservations: Create a new reservation.
GET /api/reservations: Retrieve reservations (optional filters: checkInDate, checkOutDate, roomType).
PATCH /api/reservations/{reservationId}: Update check-in date for a reservation.
DELETE /api/reservations/{reservationId}: Delete a reservation.
Room APIs
GET /api/rooms: Retrieve available rooms.

### Complex Query Example

One of the GET APIs allows retrieving reservations based on multiple parameters:

GET api/reservations?checkInDate=2026-09-22&checkOutDate=2026-09-28&roomType=FAMILY

This will return reservations filtered by check-in date, check-out date, and room type.

## Starting the Application

1. **Clone the repository:** `git clone https://github.com/AndreeCristina/Hotel-Reservation-Manager
2. **Set up database:** Create a PostgreSQL database named `hotel_resv_mgr` and configure your database connection details in the `application.properties` file.
3. **Build and run:** Use Maven to build and run the application:
    - **Maven:** `mvn clean install spring-boot:run`

The application will start on the default port (8080) and you can access the API endpoints using your preferred HTTP client.