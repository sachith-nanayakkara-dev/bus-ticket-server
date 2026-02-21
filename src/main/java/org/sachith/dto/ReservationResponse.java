package org.sachith.dto;

import java.util.List;

public class ReservationResponse {

    private String reservationId;

    private List<String> seats;

    private JourneyInfo journeyInfo;

    private int totalPrice;

    private String travelDate;

    public ReservationResponse(
            String reservationId,
            List<String> seats,
            JourneyInfo journeyInfo,
            int totalPrice,
            String travelDate) {

        this.reservationId = reservationId;
        this.seats = seats;
        this.journeyInfo = journeyInfo;
        this.totalPrice = totalPrice;
        this.travelDate = travelDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public List<String> getSeats() {
        return seats;
    }

    public JourneyInfo getJourneyInfo() {
        return journeyInfo;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getTravelDate() {
        return travelDate;
    }
}
