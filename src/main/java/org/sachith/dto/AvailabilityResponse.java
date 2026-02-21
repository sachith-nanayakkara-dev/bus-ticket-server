package org.sachith.dto;

import java.util.List;

public class AvailabilityResponse {

    private List<String> availableSeats;

    private int pricePerSeat;

    private int totalPrice;

    private String travelDate;

    public AvailabilityResponse(
            List<String> availableSeats,
            int pricePerSeat,
            int totalPrice,
            String travelDate) {

        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.totalPrice = totalPrice;
        this.travelDate = travelDate;

    }

    public List<String> getAvailableSeats() {
        return availableSeats;
    }

    public int getPricePerSeat() {
        return pricePerSeat;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getTravelDate() {
        return travelDate;
    }
}
