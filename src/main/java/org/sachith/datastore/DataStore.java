package org.sachith.datastore;

import org.sachith.model.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    public static final List<Seat> seats = new ArrayList<>();

    public static final ConcurrentHashMap<String, Object> reservations
            = new ConcurrentHashMap<>();

    // Add a counter for reservation numbers
    public static int reservationCounter = 1;

    static {

        char[] letters = {'A','B','C','D'};

        for (int row = 1; row <= 10; row++) {
            for (char letter : letters) {
                seats.add(new Seat(row + "" + letter));
            }
        }
    }
}
