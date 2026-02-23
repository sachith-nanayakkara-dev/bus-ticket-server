package org.sachith.dto;

import java.util.List;

public class AvailabilityResponse {

    private List<String> availableSeats;

    private int pricePerSeat;

    private int totalPrice;


    public AvailabilityResponse(
            List<String> availableSeats,
            int pricePerSeat,
            int totalPrice) {
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.totalPrice = totalPrice;
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

}
