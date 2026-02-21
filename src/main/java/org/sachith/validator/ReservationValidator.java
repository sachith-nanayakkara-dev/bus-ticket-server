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

        if (request.getPassengers() > 4) {
            throw new ValidationException(
                    "Maximum 4 passengers allowed per booking");
        }

        if (request.getPaymentAmount() <= 0) {
            throw new ValidationException(
                    "Payment amount must be greater than 0");
        }

        if (request.getTravelDate() == null ||
                request.getTravelDate().isBlank()) {

            throw new ValidationException("travelDate is required");
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
