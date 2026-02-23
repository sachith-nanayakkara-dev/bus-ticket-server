package org.sachith.service;

import org.sachith.constants.PricingConstants;
import org.sachith.dto.SimpleAvailabilityResponse;
import org.sachith.model.Seat;
import org.sachith.model.Trip;
import org.sachith.repository.TripRepository;
import org.sachith.repository.TripRepositoryImpl;
import org.sachith.util.LocationUtils;

import java.util.List;

public class SimpleAvailabilityServiceImpl implements SimpleAvailabilityService {

        private final TripRepository tripRepository = new TripRepositoryImpl();

        @Override
        public SimpleAvailabilityResponse getGridSeatMap(
                        String origin,
                        String destination,
                        String travelDate,
                        int passengers) {

                int originIndex = LocationUtils.index(origin);
                int destinationIndex = LocationUtils.index(destination);

                boolean isForward = originIndex < destinationIndex;

                int start = Math.min(originIndex, destinationIndex);
                int end = Math.max(originIndex, destinationIndex);

                                Trip trip = tripRepository.findOrCreate(travelDate, isForward);
                                List<Seat> seats = trip.getSeats();
                                int availableCount = 0;
                                for (Seat seat : seats) {
                                        if (seat.isAvailable(start, end, isForward)) {
                                                availableCount++;
                                        }
                                }
                                boolean availability = availableCount >= passengers;
                                int pricePerSeat = (end - start) * PricingConstants.PRICE_PER_SEGMENT;
                                int totalPrice = pricePerSeat * passengers;
                                return new SimpleAvailabilityResponse(availability, totalPrice);
        }

}
