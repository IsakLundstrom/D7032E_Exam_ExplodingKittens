package main.explodingkittens.exception;

/**
 * Exception that is thrown when an IO action failed.
 */
public class EKIOException extends EKException {
    public EKIOException() {
    }

    public EKIOException(String message) {
        super(message);
    }

    public EKIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public EKIOException(Throwable cause) {
        super(cause);
    }

    public EKIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
