package org.sachith.exception;

/**
 * Custom exception for business logic errors.
 * Used to signal application-specific error conditions with an error code.
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode, String message) {

        super(message);

        this.errorCode = errorCode;
    }

    public String getErrorCode() {

        return errorCode;
    }
}
