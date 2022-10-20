package main.explodingkittens.exception;

/**
 * Exception that are thrown by a network component.
 */
public class EKNetworkException extends Exception {
    public EKNetworkException() {
    }

    public EKNetworkException(String message) {
        super(message);
    }

    public EKNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public EKNetworkException(Throwable cause) {
        super(cause);
    }

    //TODO : remove maybe
    public EKNetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
