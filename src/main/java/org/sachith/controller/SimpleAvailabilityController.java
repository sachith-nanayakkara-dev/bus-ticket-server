package org.sachith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dispatcher.Controller;
import org.sachith.dispatcher.RequestContext;
import org.sachith.dto.SimpleAvailabilityResponse;
import org.sachith.service.SimpleAvailabilityService;
import org.sachith.service.SimpleAvailabilityServiceImpl;

public class SimpleAvailabilityController implements Controller {

    private final SimpleAvailabilityService service = new SimpleAvailabilityServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Controller for handling seat grid map queries.
     * Accepts origin, destination, and travelDate parameters,
     * validates input, and returns seat map information as JSON.
     */

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext ctx = new RequestContext(mapper, request, response);
        String origin = ctx.queryParam("origin");
        String destination = ctx.queryParam("destination");
        String travelDate = ctx.queryParam("travelDate");
        String passengersStr = ctx.queryParam("passengers");
        int passengers = 1;
        if (passengersStr != null && !passengersStr.isEmpty()) {
            try {
                passengers = Integer.parseInt(passengersStr);
            } catch (NumberFormatException ex) {
                ctx.writeJson(400, java.util.Map.of("error", "Invalid passengers parameter"));
                return;
            }
        }

        // Simple validation: check for missing or empty params
        if (origin == null || origin.isEmpty() ||
                destination == null || destination.isEmpty() ||
                travelDate == null || travelDate.isEmpty()) {
            ctx.writeJson(400, java.util.Map.of("error", "Missing or empty required parameter(s)"));
            return;
        }

        // Validate allowed values for origin and destination
        String[] allowed = {"A", "B", "C", "D"};
        boolean validOrigin = java.util.Arrays.asList(allowed).contains(origin.toUpperCase());
        boolean validDestination = java.util.Arrays.asList(allowed).contains(destination.toUpperCase());
        if (!validOrigin || !validDestination) {
            ctx.writeJson(400, java.util.Map.of("error", "Origin and destination must be one of A, B, C, D"));
            return;
        }

        // Validate origin and destination not same
        if (origin.equalsIgnoreCase(destination)) {
            ctx.writeJson(400, java.util.Map.of("error", "Origin and destination cannot be the same"));
            return;
        }

        // Validate travelDate format (yyyy-MM-dd)
        try {
            java.time.LocalDate.parse(travelDate);
        } catch (Exception e) {
            ctx.writeJson(400, java.util.Map.of("error", "Invalid travelDate format, expected yyyy-MM-dd"));
            return;
        }

        SimpleAvailabilityResponse res = service.getGridSeatMap(origin, destination, travelDate, passengers);
        ctx.writeJson(200, res);
    }
}
