package org.sachith.validator;

import org.sachith.dto.AvailabilityRequest;
import org.sachith.exception.ValidationException;

import java.util.Set;

public class AvailabilityValidator {

    private static final Set<String> VALID_LOCATIONS =
            Set.of("A", "B", "C", "D");

    public static void validate(AvailabilityRequest request) {

        if (request == null) {
            throw new ValidationException("Request body is required");
        }

        validateLocation(request.getOrigin(), "origin");

        validateLocation(request.getDestination(), "destination");

        DateValidator.validate(request.getTravelDate());

        if (request.getOrigin().equals(request.getDestination())) {
            throw new ValidationException(
                    "Origin and destination cannot be same");
        }

        if (request.getPassengers() <= 0) {
            throw new ValidationException(
                    "Passengers must be greater than 0");
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
