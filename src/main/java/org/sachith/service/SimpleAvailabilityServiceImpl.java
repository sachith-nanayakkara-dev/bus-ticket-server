package org.sachith.service;

import org.sachith.dto.SimpleAvailabilityResponse;
import org.sachith.model.Seat;
import org.sachith.model.Trip;
import org.sachith.repository.TripRepository;
import org.sachith.repository.TripRepositoryImpl;

import java.util.List;

public class SimpleAvailabilityServiceImpl implements SimpleAvailabilityService {

        private final TripRepository tripRepository = new TripRepositoryImpl();

        @Override
        public SimpleAvailabilityResponse getGridSeatMap(
                        String origin,
                        String destination,
                        String travelDate,
                        int passengers) {

                int originIndex = index(origin);
                int destinationIndex = index(destination);

                boolean isForward = originIndex < destinationIndex;

                int start = Math.min(index(origin), index(destination));
                int end = Math.max(index(origin), index(destination));

                                Trip trip = tripRepository.findOrCreate(travelDate, isForward);
                                List<Seat> seats = trip.getSeats();
                                int availableCount = 0;
                                for (Seat seat : seats) {
                                        if (seat.isAvailable(start, end, isForward)) {
                                                availableCount++;
                                        }
                                }
                                boolean availability = availableCount >= passengers;
                                int pricePerSeat = (end - start) * 50;
                                int totalPrice = pricePerSeat * passengers;
                                return new SimpleAvailabilityResponse(availability, totalPrice);
        }

        private int index(String location) {

                return switch (location) {

                        case "A" -> 0;
                        case "B" -> 1;
                        case "C" -> 2;
                        case "D" -> 3;
                        default -> throw new RuntimeException("Invalid");
                };
        }
}
