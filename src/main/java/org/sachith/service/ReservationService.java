
package org.sachith.service;

import org.sachith.dto.ReservationResponse;

/**
 * Service interface for reserving bus seats.
 */
public interface ReservationService {

    /**
     * Reserves seats for a bus trip.
     * 
     * @param origin        Starting location
     * @param destination   Ending location
     * @param passengers    Number of passengers
     * @param paymentAmount Payment amount for reservation
     * @param travelDate    Date of travel (yyyy-MM-dd)
     * @return ReservationResponse with reservation details
     */
    public ReservationResponse reserve(
            String origin,
            String destination,
            int passengers,
            int paymentAmount,
            String travelDate);
}
