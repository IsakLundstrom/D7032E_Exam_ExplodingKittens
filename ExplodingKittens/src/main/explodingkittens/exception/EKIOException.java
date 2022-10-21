package main.explodingkittens.exception;

/**
 * Exception that are thrown when an IO action failed.
 */
public class EKIOException extends Exception {
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

}
