package org.sachith.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sachith.dto.ReservationResponse;
import org.sachith.repository.TripRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationConcurrencyTest {

    private ReservationService reservationService;
    private AvailabilityService availabilityService;

    @BeforeEach
    void setup() {

        // Reset in-memory state before test
        TripRepositoryImpl.clear();

        reservationService = new ReservationServiceImpl();
        availabilityService = new AvailabilityServiceImpl();
    }

    @Test
    void shouldNotAllowOverbookingUnderConcurrentRequests()
            throws InterruptedException {

        int threadCount = 50;

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch =
                new CountDownLatch(threadCount);

        List<Future<List<String>>> futures =
                new ArrayList<>();

        String travelDate = "2026-03-20";

        // Each thread tries to book 1 seat
        for (int i = 0; i < threadCount; i++) {

            futures.add(
                    executor.submit(() -> {

                        try {

                            ReservationResponse response =
                                    reservationService.reserve(
                                            "A",
                                            "D",
                                            1,
                                            150,
                                            travelDate,
                                            null
                                    );

                            return response.getSeats();

                        } catch (Exception ex) {

                            return List.of(); // booking failed
                        } finally {

                            latch.countDown();
                        }
                    })
            );
        }

        latch.await();

        executor.shutdown();

        // Count total booked seats
        int totalBookedSeats = 0;

        for (Future<List<String>> future : futures) {

            try {
                totalBookedSeats += future.get().size();
            } catch (Exception ignored) {}
        }

        // Verify system never booked more than 40 seats
        assertTrue(totalBookedSeats <= 40);

        // Verify availability matches expected remaining seats
        var availability =
                availabilityService.checkAvailability(
                        "A",
                        "D",
                        40,
                        travelDate
                );

        assertEquals(
                40 - totalBookedSeats,
                availability.getAvailableSeats().size()
        );
    }
}
