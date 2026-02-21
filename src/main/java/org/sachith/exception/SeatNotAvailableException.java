package org.sachith.exception;

public class SeatNotAvailableException extends BusinessException {

    public SeatNotAvailableException() {

        super("SEAT_NOT_AVAILABLE",
                "Not enough seats available");
    }
}
