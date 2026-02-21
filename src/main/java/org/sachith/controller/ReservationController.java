package org.sachith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sachith.dispatcher.Controller;
import org.sachith.dto.ReservationRequest;
import org.sachith.dto.ReservationResponse;
import org.sachith.service.ReservationService;
import org.sachith.service.ReservationServiceImpl;
import org.sachith.validator.ReservationValidator;

public class ReservationController implements Controller {

    private final ReservationService service = new ReservationServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ReservationRequest req =
                mapper.readValue(request.getInputStream(), ReservationRequest.class);

        ReservationValidator.validate(req);

        ReservationResponse res =
                service.reserve(req.getOrigin(), req.getDestination(), req.getPassengers(), req.getPaymentAmount(), req.getTravelDate());

        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), res);
    }
}
