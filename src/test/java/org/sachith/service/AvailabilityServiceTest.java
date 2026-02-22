package org.sachith.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sachith.dto.AvailabilityResponse;

import static org.junit.jupiter.api.Assertions.*;

public class AvailabilityServiceTest {

    private AvailabilityService availabilityService;

    @BeforeEach
    void setup() {

        availabilityService =
                new AvailabilityServiceImpl();
    }

    /**
     * Test forward journey availability
     * Example: A → C
     */
    @Test
    void shouldReturnAvailableSeatsForForwardJourney() {

        AvailabilityResponse response =
                availabilityService.checkAvailability(
                        "A",
                        "C",
                        2,
                        "2026-03-01"
                );

        assertNotNull(response);

        assertEquals(40,
                response.getAvailableSeats().size());

        assertEquals(100,
                response.getPricePerSeat());

        assertEquals(200,
                response.getTotalPrice());
    }

    /**
     * Test return journey availability
     * Example: D → B
     */
    @Test
    void shouldReturnAvailableSeatsForReturnJourney() {

        AvailabilityResponse response =
                availabilityService.checkAvailability(
                        "D",
                        "B",
                        3,
                        "2026-03-01"
                );

        assertNotNull(response);

        assertEquals(40,
                response.getAvailableSeats().size());

        assertEquals(100,
                response.getPricePerSeat());

        assertEquals(300,
                response.getTotalPrice());
    }

    /**
     * Test date isolation
     *
     * Booking on one date must NOT affect another date
     */

    @Test
    void shouldReturnIndependentAvailabilityForDifferentDates() {

        AvailabilityService availabilityService =
                new AvailabilityServiceImpl();

        ReservationService reservationService =
                new ReservationServiceImpl();

        // Date 1: reserve 40 seats for A -> D (3 segments => 150 per seat => 6000 for 40)
        reservationService.reserve(
                "A",
                "D",
                40,
                6000,
                "2026-03-01",
                null
        );

        // Date 1: now should have 0 seats available for A -> D
        AvailabilityResponse date1AfterBooking =
                availabilityService.checkAvailability(
                        "A",
                        "D",
                        40,
                        "2026-03-01"
                );

        // Date 2: should still have 40 seats available
        AvailabilityResponse date2 =
                availabilityService.checkAvailability(
                        "A",
                        "D",
                        40,
                        "2026-03-02"
                );

        assertEquals(0, date1AfterBooking.getAvailableSeats().size());
        assertEquals(40, date2.getAvailableSeats().size());
    }

}
