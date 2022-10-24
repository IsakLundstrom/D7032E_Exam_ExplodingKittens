package main.explodingkittens.exception;

/**
 * Exception that is thrown by a network component.
 */
public class EKNetworkException extends EKException {
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

    public EKNetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
