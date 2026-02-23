package org.sachith.validator;

import org.sachith.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateValidator {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);

    public static void validate(String date) {

        if (date == null || date.isBlank()) {
            throw new ValidationException("travelDate is required");
        }

        try {

            LocalDate parsed =
                    LocalDate.parse(date, FORMATTER);

            if (parsed.isBefore(LocalDate.now())) {
                throw new ValidationException(
                        "travelDate cannot be in the past");
            }

        } catch (Exception ex) {

            throw new ValidationException(
                    "Invalid travelDate");
        }
    }
}
