package org.sachith.service;

import org.sachith.dto.ReservationResponse;

public interface ReservationService {
    public ReservationResponse reserve(
            String origin,
            String destination,
            int passengers,
            int paymentAmount,
            String travelDate) ;
}
