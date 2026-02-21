package org.sachith.service;

import org.sachith.dto.AvailabilityResponse;

public interface AvailabilityService {

    public AvailabilityResponse checkAvailability(
            String origin,
            String destination,
            int passengers,
            String travelDate);
}
