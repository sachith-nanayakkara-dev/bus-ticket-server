package org.sachith.model;

import java.util.List;

public class Trip {

    //todo:check this
    private String tripId;
    private String travelDate;
    private boolean forward;

    private List<Seat> seats;

    public Trip(String tripId, String travelDate, boolean forward, List<Seat> seats) {
        this.tripId = tripId;
        this.travelDate = travelDate;
        this.forward = forward;
        this.seats = seats;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public boolean isForward() {
        return forward;
    }
}
