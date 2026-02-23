package org.sachith.dto;

public class SimpleAvailabilityResponse {

    private boolean availability;
    private int totalPrice;

    public SimpleAvailabilityResponse(boolean availability, int totalPrice) {
        this.availability = availability;
        this.totalPrice = totalPrice;
    }
    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean isAvailability() {
        return availability;
    }
}
