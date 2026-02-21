package org.sachith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dispatcher.Controller;
import org.sachith.dto.GridSeatMapResponse;
import org.sachith.service.SeatGridQueryService;
import org.sachith.service.SeatGridQueryServiceImpl;

public class SeatGridMapController implements Controller {

    private final SeatGridQueryService service = new SeatGridQueryServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Controller for handling seat grid map queries.
     * Accepts origin, destination, and travelDate parameters,
     * validates input, and returns seat map information as JSON.
     */

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");
        String travelDate = request.getParameter("travelDate");


        // Simple validation: check for missing or empty params
        if (origin == null || origin.isEmpty() ||
            destination == null || destination.isEmpty() ||
            travelDate == null || travelDate.isEmpty()) {
            response.setStatus(400);
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(),
                java.util.Map.of("error", "Missing or empty required parameter(s)"));
            return;
        }

        // Validate travelDate format (yyyy-MM-dd)
        try {
            java.time.LocalDate.parse(travelDate);
        } catch (Exception e) {
            response.setStatus(400);
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(),
                java.util.Map.of("error", "Invalid travelDate format, expected yyyy-MM-dd"));
            return;
        }

        GridSeatMapResponse res = service.getGridSeatMap(origin, destination, travelDate);

        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), res);
    }
}
