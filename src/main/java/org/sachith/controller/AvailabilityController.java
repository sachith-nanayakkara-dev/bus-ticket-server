package org.sachith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dispatcher.Controller;
import org.sachith.dispatcher.RequestContext;
import org.sachith.dto.AvailabilityRequest;
import org.sachith.dto.AvailabilityResponse;
import org.sachith.service.AvailabilityService;
import org.sachith.service.AvailabilityServiceImpl;
import org.sachith.validator.AvailabilityValidator;

/**
 * Controller for handling seat availability requests.
 */
public class AvailabilityController implements Controller {

    private final AvailabilityService service = new AvailabilityServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext ctx = new RequestContext(mapper, request, response);
        AvailabilityRequest req = ctx.readBody(AvailabilityRequest.class);
        AvailabilityValidator.validate(req);
        AvailabilityResponse res = service.checkAvailability(
                req.getOrigin(), req.getDestination(), req.getPassengers(), req.getTravelDate());
        ctx.writeJson(200, res);
    }
}
