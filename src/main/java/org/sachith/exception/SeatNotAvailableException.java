package org.sachith.exception;

public class SeatNotAvailableException extends BusinessException {

    public SeatNotAvailableException() {
        super("SEAT_NOT_AVAILABLE", "Not enough seats available");
    }

    public SeatNotAvailableException(String message) {
        super("SEAT_NOT_AVAILABLE", message);
    }

    public SeatNotAvailableException(String errorCode, String message) {
        super(errorCode, message);
    }

    
}
