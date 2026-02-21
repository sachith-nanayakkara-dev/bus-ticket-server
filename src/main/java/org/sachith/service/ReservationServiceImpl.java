package org.sachith.service;

import org.sachith.dto.JourneyInfo;
import org.sachith.dto.ReservationResponse;
import org.sachith.exception.InvalidPaymentException;
import org.sachith.exception.SeatNotAvailableException;
import org.sachith.model.Reservation;
import org.sachith.model.Seat;
import org.sachith.model.Trip;
import org.sachith.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private static final Logger log =
            LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository =
            new ReservationRepositoryImpl();

    private final ScheduleService scheduleService =
            new ScheduleService();

    private final TripRepository tripRepository = new TripRepositoryImpl();

    @Override
    public synchronized ReservationResponse reserve(
            String origin,
            String destination,
            int passengers,
            int paymentAmount,
            String travelDate) {

        log.info("Reservation request received: origin={}, destination={}, passengers={}",
                origin, destination, passengers);

        int originIndex = index(origin);
        int destinationIndex = index(destination);

        boolean isForward =
                index(origin) < index(destination);

        int start = Math.min(originIndex, destinationIndex);
        int end = Math.max(originIndex, destinationIndex);


        int pricePerSeat = (end - start) * 50;
        int expectedTotal = pricePerSeat * passengers;

        if (paymentAmount != expectedTotal) {

            log.warn("Invalid payment amount: expected={}, actual={}",
                    expectedTotal, paymentAmount);

            throw new InvalidPaymentException();
        }

        List<String> bookedSeats = new ArrayList<>();

        boolean forward = originIndex < destinationIndex;

        Trip trip =
                tripRepository.findOrCreate(travelDate, forward);

        for (Seat seat : trip.getSeats()) {

            if (seat.isAvailable(start, end, isForward)) {

                seat.reserve(start, end, isForward);

                bookedSeats.add(seat.getSeatNumber());

                if (bookedSeats.size() == passengers)
                    break;
            }
        }

        if (bookedSeats.size() != passengers) {

            log.warn("Not enough seats available for route {} → {}", origin, destination);

            throw new SeatNotAvailableException();
        }

        // Generate reservation ID
        String reservationId =
                reservationRepository.generateReservationId(origin, destination);

        // Create Reservation domain object
        Reservation reservation = new Reservation();

        reservation.setReservationId(reservationId);
        reservation.setOrigin(origin);
        reservation.setDestination(destination);
        reservation.setSeats(bookedSeats);
        reservation.setTotalPrice(expectedTotal);

        // Save using repository
        reservationRepository.save(reservation);

        log.info("Reservation successful: reservationId={}, seats={}",
                reservationId, bookedSeats);
        JourneyInfo journeyInfo =
                scheduleService.calculateJourney(origin, destination);

        return new ReservationResponse(
                reservationId,
                bookedSeats,
                journeyInfo,
                expectedTotal,
                travelDate
        );
    }

    private int index(String location) {

        return switch (location) {

            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> throw new IllegalArgumentException("Invalid location: " + location);
        };
    }
}
