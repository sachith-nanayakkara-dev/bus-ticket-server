package org.sachith.exception;

public class InvalidPaymentException extends BusinessException {

    public InvalidPaymentException() {

        super("INVALID_PAYMENT",
                "Payment amount is incorrect");
    }
}
