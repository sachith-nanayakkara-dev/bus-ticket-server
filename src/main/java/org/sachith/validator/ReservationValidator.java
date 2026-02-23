package org.sachith.validator;

import org.sachith.dto.ReservationRequest;
import org.sachith.exception.ValidationException;

import java.util.Set;

public class ReservationValidator {

    private static final Set<String> VALID_LOCATIONS =
            Set.of("A", "B", "C", "D");

    public static void validate(ReservationRequest request) {

        if (request == null) {
            throw new ValidationException("Request body is required");
        }

        validateLocation(request.getOrigin(), "origin");

        validateLocation(request.getDestination(), "destination");

        // travelDate validation
        DateValidator.validate(request.getTravelDate());

        if (request.getOrigin().equals(request.getDestination())) {
            throw new ValidationException(
                    "Origin and destination cannot be the same");
        }

        if (request.getPassengers() <= 0) {
            throw new ValidationException(
                    "Passengers must be greater than 0");
        }

        if (request.getPaymentAmount() <= 0) {
            throw new ValidationException(
                    "Payment amount must be greater than 0");
        }

        if (request.getTravelDate() == null ||
                request.getTravelDate().isBlank()) {
            throw new ValidationException("travelDate is required");
        }

        // Validate seats if provided
        if (request.getSeats() != null && !request.getSeats().isEmpty()) {
            if (request.getSeats().size() != request.getPassengers()) {
                throw new ValidationException("Number of seats must match passengers");
            }
            for (String seatNum : request.getSeats()) {
                if (seatNum == null || seatNum.isBlank()) {
                    throw new ValidationException("Seat number cannot be blank");
                }
                // Optionally: validate seat format (e.g., 1A, 10D)
                if (!seatNum.matches("^(10|[1-9])[A-D]$")) {
                    throw new ValidationException("Invalid seat number: " + seatNum);
                }
            }
        }

    }

    private static void validateLocation(
            String location,
            String fieldName) {

        if (location == null || location.isBlank()) {
            throw new ValidationException(
                    fieldName + " is required");
        }

        if (!VALID_LOCATIONS.contains(location)) {
            throw new ValidationException(
                    "Invalid " + fieldName + ": " + location);
        }
    }
}
