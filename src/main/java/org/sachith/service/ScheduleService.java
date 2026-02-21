
package org.sachith.service;

import org.sachith.dto.JourneyInfo;

/**
 * Service interface for calculating journey information for a bus trip.
 */
public interface ScheduleService {

        /**
         * Calculates journey details (departure and arrival times) for a given route.
         * 
         * @param origin      Starting location
         * @param destination Ending location
         * @return JourneyInfo with formatted times
         */
        public JourneyInfo calculateJourney(
                        String origin,
                        String destination);
}
