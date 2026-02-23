package org.sachith.service;

import org.sachith.constants.PricingConstants;
import org.sachith.dto.JourneyInfo;
import org.sachith.dto.ReservationResponse;
import org.sachith.exception.InvalidPaymentException;
import org.sachith.exception.SeatNotAvailableException;
import org.sachith.model.Reservation;
import org.sachith.model.Seat;
import org.sachith.model.Trip;
import org.sachith.repository.*;
import org.sachith.util.LocationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

        private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

        private final ReservationRepository reservationRepository = new ReservationRepositoryImpl();

        private final ScheduleServiceImpl scheduleServiceImpl = new ScheduleServiceImpl();

        private final TripRepository tripRepository = new TripRepositoryImpl();

        @Override
        public synchronized ReservationResponse reserve(
                        String origin,
                        String destination,
                        int passengers,
                        int paymentAmount,
                        String travelDate,
                        List<String> seats) {

                log.info("Reservation request received: origin={}, destination={}, passengers={}",
                                origin, destination, passengers);

                int originIndex = LocationUtils.index(origin);
                int destinationIndex = LocationUtils.index(destination);

                boolean isForward = originIndex < destinationIndex;

                // 'start' is the lower index of the two locations (inclusive), marking the
                // beginning segment of the journey.
                int start = Math.min(originIndex, destinationIndex);
                // 'end' is the higher index of the two locations (exclusive), marking the
                // segment just after the journey ends.
                int end = Math.max(originIndex, destinationIndex);

                // calculate price per seat & expected total
                int pricePerSeat = (end - start) * PricingConstants.PRICE_PER_SEGMENT;
                int expectedTotal = pricePerSeat * passengers;

                if (paymentAmount != expectedTotal) {

                        log.warn("Invalid payment amount: expected={}, actual={}",
                                        expectedTotal, paymentAmount);

                        throw new InvalidPaymentException();
                }

                List<String> bookedSeats = new ArrayList<>();
                Trip trip = tripRepository.findOrCreate(travelDate, isForward);

                if (seats != null && !seats.isEmpty()) {
                        // Client requested specific seats
                        for (String seatNum : seats) {
                                Seat seat = trip.getSeats().stream()
                                                .filter(s -> s.getSeatNumber().equals(seatNum))
                                                .findFirst()
                                                .orElse(null);
                                if (seat == null || !seat.isAvailable(start, end, isForward)) {
                                        String msg = String.format(
                                                        "Requested seat %s is not available for route %s → %s", seatNum,
                                                        origin, destination);
                                        log.warn(msg);
                                        throw new SeatNotAvailableException(msg);
                                }
                        }
                        // All requested seats are available, reserve them
                        for (String seatNum : seats) {
                                Seat seat = trip.getSeats().stream()
                                                .filter(s -> s.getSeatNumber().equals(seatNum))
                                                .findFirst()
                                                .orElse(null);
                                seat.reserve(start, end, isForward);
                                bookedSeats.add(seatNum);
                        }
                        if (bookedSeats.size() != passengers) {
                                String msg = String.format(
                                                "Number of requested seats does not match passengers for route %s → %s",
                                                origin, destination);
                                log.warn(msg);
                                throw new SeatNotAvailableException(msg);
                        }
                } else {
                        // Auto-assign seats: atomic reservation
                        List<Seat> availableSeats = new ArrayList<>();
                        for (Seat seat : trip.getSeats()) {
                                if (seat.isAvailable(start, end, isForward)) {
                                        availableSeats.add(seat);
                                        if (availableSeats.size() == passengers)
                                                break;
                                }
                        }
                        if (availableSeats.size() != passengers) {
                                String msg = String.format("Not enough seats available for route %s → %s", origin,
                                                destination);
                                log.warn(msg);
                                throw new SeatNotAvailableException(msg);
                        }
                        // Now reserve all seats
                        for (Seat seat : availableSeats) {
                                seat.reserve(start, end, isForward);
                                bookedSeats.add(seat.getSeatNumber());
                        }
                }

                // Generate reservation ID
                String reservationId = reservationRepository.generateReservationId(origin, destination);

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
                JourneyInfo journeyInfo = scheduleServiceImpl.calculateJourney(origin, destination);

                return new ReservationResponse(
                                reservationId,
                                bookedSeats,
                                journeyInfo,
                                expectedTotal,
                                travelDate);
        }

}
