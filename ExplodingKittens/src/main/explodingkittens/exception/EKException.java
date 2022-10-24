package main.explodingkittens.exception;

/**
 * Exception that is thrown by this application
 */
public class EKException extends Exception {
    public EKException() {
    }

    public EKException(String message) {
        super(message);
    }

    public EKException(String message, Throwable cause) {
        super(message, cause);
    }

    public EKException(Throwable cause) {
        super(cause);
    }

    public EKException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
