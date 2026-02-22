package org.sachith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dispatcher.Controller;
import org.sachith.dispatcher.RequestContext;
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
        RequestContext ctx = new RequestContext(mapper, request, response);
        String origin = ctx.queryParam("origin");
        String destination = ctx.queryParam("destination");
        String travelDate = ctx.queryParam("travelDate");

        // Simple validation: check for missing or empty params
        if (origin == null || origin.isEmpty() ||
                destination == null || destination.isEmpty() ||
                travelDate == null || travelDate.isEmpty()) {
            ctx.writeJson(400, java.util.Map.of("error", "Missing or empty required parameter(s)"));
            return;
        }

        // Validate travelDate format (yyyy-MM-dd)
        try {
            java.time.LocalDate.parse(travelDate);
        } catch (Exception e) {
            ctx.writeJson(400, java.util.Map.of("error", "Invalid travelDate format, expected yyyy-MM-dd"));
            return;
        }

        GridSeatMapResponse res = service.getGridSeatMap(origin, destination, travelDate);
        ctx.writeJson(200, res);
    }
}
