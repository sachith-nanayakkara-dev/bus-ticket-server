package org.sachith.service;

import org.sachith.dto.AvailabilityResponse;

/**
 * Service interface for checking seat availability for a bus trip.
 */
public interface AvailabilityService {

    /**
     * Checks the availability of seats for a given trip based on origin,
     * destination, number of passengers, and travel date.
     * 
     * @param origin      the starting point of the trip
     * @param destination the ending point of the trip
     * @param passengers  the number of passengers
     * @param travelDate  the date of travel
     * @return an AvailabilityResponse containing the available seats and pricing
     *         information
     */
    public AvailabilityResponse checkAvailability(
            String origin,
            String destination,
            int passengers,
            String travelDate);
}
