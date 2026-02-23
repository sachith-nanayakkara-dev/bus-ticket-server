package org.sachith.util;

public class LocationUtils {
    /**
     * Returns the index for a location (A, B, C, D).
     * Throws IllegalArgumentException for invalid locations.
     */
    public static int index(String location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        return switch (location.toUpperCase()) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> throw new IllegalArgumentException("Invalid location: " + location);
        };
    }
}
