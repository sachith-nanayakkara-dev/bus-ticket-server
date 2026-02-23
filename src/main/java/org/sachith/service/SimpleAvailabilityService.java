package org.sachith.service;

import org.sachith.dto.SimpleAvailabilityResponse;

public interface SimpleAvailabilityService {

        SimpleAvailabilityResponse getGridSeatMap(
            String origin,
            String destination,
            String travelDate,
            int passengers);
}
