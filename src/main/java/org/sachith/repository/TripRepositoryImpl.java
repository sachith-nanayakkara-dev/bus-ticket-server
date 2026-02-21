package org.sachith.repository;

import org.sachith.model.Seat;
import org.sachith.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TripRepositoryImpl implements TripRepository {

    private static final ConcurrentHashMap<String, Trip> trips =
            new ConcurrentHashMap<>();

    @Override
    public Trip findOrCreate(String date, boolean forward) {

        String key = date + "_" + (forward ? "F" : "R");

        //todo: check
        return trips.computeIfAbsent(key, k -> {

            List<Seat> seats = new ArrayList<>();

            for (int row = 1; row <= 10; row++) {
                for (char col = 'A'; col <= 'D'; col++) {
                    seats.add(new Seat(row + "" + col));
                }
            }

            return new Trip(k, date, forward, seats);
        });
    }

    public static void clear() {
        trips.clear();
    }
}
