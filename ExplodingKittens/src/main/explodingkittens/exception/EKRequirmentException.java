package main.explodingkittens.exception;

/**
 * Exception that is thrown when a requirement is breached
 */
public class EKRequirmentException extends EKException {
    public EKRequirmentException() {
    }

    public EKRequirmentException(String message) {
        super(message);
    }

    public EKRequirmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EKRequirmentException(Throwable cause) {
        super(cause);
    }

    public EKRequirmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
