# Bus Ticket Server

A simple Java web application for bus seat reservation and availability management.

## Features
- Seat reservation with payment validation
- Seat availability queries
- Seat map grid for each trip/date
- Exception handling with JSON error responses
- Date-based trip and seat management

## Requirements
- Java 17 or higher
- Gradle 8+

## How to Build
```
./gradlew build
```

## How to Run
```
./gradlew run
```

## How to Run Tests
```
./gradlew test
```
Or to run a specific test:
```
./gradlew test --tests org.sachith.service.AvailabilityServiceTest
```

## API Endpoints
- `POST /reserve` — Reserve seats (JSON body)
- `GET /availability?origin=A&destination=B&passengers=1&travelDate=2026-02-20` — Check seat availability
- `GET /seat-map/grid?origin=A&destination=B&travelDate=2026-02-20` — Get seat map grid

## Notes
- All seat and trip operations are scoped by travelDate and direction.
- Error responses are returned in JSON format with error codes.
- See source code for more details on request/response formats.
