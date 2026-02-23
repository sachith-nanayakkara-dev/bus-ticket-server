package org.sachith.service;

import org.sachith.dto.AvailabilityResponse;
import org.sachith.exception.SeatNotAvailableException;
import org.sachith.model.Seat;
import org.sachith.model.Trip;
import org.sachith.repository.TripRepository;
import org.sachith.repository.TripRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityServiceImpl implements AvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    private final TripRepository tripRepository = new TripRepositoryImpl();

    @Override
    public AvailabilityResponse checkAvailability(
            String origin,
            String destination,
            int passengers,
            String travelDate) {

        log.info("Checking availability: origin={}, destination={}, passengers={}, travelDate={}",
                origin, destination, passengers, travelDate);

        int originIndex = index(origin);
        int destinationIndex = index(destination);

        boolean isForward = originIndex < destinationIndex;

        int start = Math.min(originIndex, destinationIndex);
        int end = Math.max(originIndex, destinationIndex);

        // Fetch trip based on date and direction
        Trip trip = tripRepository.findOrCreate(travelDate, isForward);

        List<String> availableSeats = new ArrayList<>();

        for (Seat seat : trip.getSeats()) {
            if (seat.isAvailable(start, end, isForward)) {
                availableSeats.add(seat.getSeatNumber());
            }
        }

        if (availableSeats.size() < passengers) {
            throw new SeatNotAvailableException(
                    "Not enough seats available for the requested number of passengers");
        }

        int pricePerSeat = (end - start) * 50;
        int totalPrice = pricePerSeat * passengers;

        return new AvailabilityResponse(
                availableSeats,
                pricePerSeat,
                totalPrice);
    }

    private int index(String location) {

        return switch (location) {

            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> throw new RuntimeException("Invalid location");
        };
    }
}
