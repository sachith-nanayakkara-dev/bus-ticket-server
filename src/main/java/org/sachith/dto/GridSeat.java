package org.sachith.dto;

public class GridSeat {

    private String seatNumber;
    private String status;

    public GridSeat(String seatNumber, String status) {

        this.seatNumber = seatNumber;
        this.status = status;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }
}
