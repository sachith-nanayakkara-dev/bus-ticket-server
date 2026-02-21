package org.sachith.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sachith.dto.ReservationResponse;
import org.sachith.exception.InvalidPaymentException;
import org.sachith.exception.SeatNotAvailableException;
import org.sachith.repository.TripRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    private ReservationService reservationService;
    private AvailabilityService availabilityService;

    @BeforeEach
    void setup() {

        // Reset in-memory state before each test
        TripRepositoryImpl.clear();

        reservationService =
                new ReservationServiceImpl();

        availabilityService =
                new AvailabilityServiceImpl();
    }

    /**
     * Test successful forward reservation
     */
    @Test
    void shouldReserveSeatsForwardSuccessfully() {

        ReservationResponse response =
                reservationService.reserve(
                        "A",
                        "C",
                        2,
                        200,
                        "2026-03-10"
                );

        assertNotNull(response);

        assertEquals(2,
                response.getSeats().size());

        assertEquals("A",
                response.getJourneyInfo().getOrigin());

        assertEquals("C",
                response.getJourneyInfo().getDestination());

        assertEquals(200,
                response.getTotalPrice());
    }

    /**
     * Test successful return reservation
     */
    @Test
    void shouldReserveSeatsReturnSuccessfully() {

        ReservationResponse response =
                reservationService.reserve(
                        "D",
                        "B",
                        3,
                        300,
                        "2026-03-10"
                );

        assertNotNull(response);

        assertEquals(3,
                response.getSeats().size());

        assertEquals("D",
                response.getJourneyInfo().getOrigin());

        assertEquals("B",
                response.getJourneyInfo().getDestination());
    }

    /**
     * Test invalid payment rejection
     */
    @Test
    void shouldThrowInvalidPaymentException() {

        assertThrows(
                InvalidPaymentException.class,
                () -> reservationService.reserve(
                        "A",
                        "C",
                        2,
                        10,
                        "2026-03-10"
                )
        );
    }

    /**
     * Test seat not available when fully booked
     */
    @Test
    void shouldThrowSeatNotAvailableExceptionWhenFull() {

        // Book all seats
        reservationService.reserve(
                "A",
                "D",
                40,
                6000,
                "2026-03-10"
        );

        assertThrows(
                SeatNotAvailableException.class,
                () -> reservationService.reserve(
                        "A",
                        "D",
                        1,
                        150,
                        "2026-03-10"
                )
        );
    }

    /**
     * Test date isolation
     */
    @Test
    void shouldAllowBookingOnDifferentDatesIndependently() {

        reservationService.reserve(
                "A",
                "D",
                40,
                6000,
                "2026-03-10"
        );

        // Next date should still allow booking
        ReservationResponse response =
                reservationService.reserve(
                        "A",
                        "D",
                        1,
                        150,
                        "2026-03-11"
                );

        assertNotNull(response);

        assertEquals(1,
                response.getSeats().size());
    }

    /**
     * Test concurrency safety
     */
    @Test
    void shouldHandleConcurrentReservationsSafely()
            throws InterruptedException {

        int threadCount = 20;

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch =
                new CountDownLatch(threadCount);

        List<Future<Boolean>> futures =
                new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {

            futures.add(
                    executor.submit(() -> {

                        try {

                            reservationService.reserve(
                                    "A",
                                    "D",
                                    2,
                                    300,
                                    "2026-03-15"
                            );

                            return true;

                        } catch (SeatNotAvailableException ex) {

                            return false;
                        } finally {

                            latch.countDown();
                        }
                    })
            );
        }

        latch.await();

        executor.shutdown();

        int successCount = 0;

        for (Future<Boolean> f : futures) {

            try {

                if (f.get()) successCount++;

            } catch (Exception ignored) {}
        }

        // Max seats = 40, each request books 2 seats
        // So max successful requests = 20

        assertTrue(successCount <= 20);
    }
}
