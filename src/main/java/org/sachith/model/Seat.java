package org.sachith.model;


public class Seat {

    private final String seatNumber;

    private final boolean[] forwardSegments = new boolean[3];

    private final boolean[] returnSegments = new boolean[3];

    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public synchronized boolean isAvailable(
            int start,
            int end,
            boolean isForward) {

        boolean[] segments =
                isForward ? forwardSegments : returnSegments;

        for (int i = start; i < end; i++) {

            if (segments[i]) return false;
        }

        return true;
    }

    public synchronized void reserve(
            int start,
            int end,
            boolean isForward) {

        boolean[] segments =
                isForward ? forwardSegments : returnSegments;

        for (int i = start; i < end; i++) {

            segments[i] = true;
        }
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}


