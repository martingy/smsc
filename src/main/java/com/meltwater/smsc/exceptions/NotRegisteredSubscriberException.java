package com.meltwater.smsc.exceptions;

/**
 * thrown when a Subscriber is not registered in the SMSC
 */
public class NotRegisteredSubscriberException extends Exception {

    public NotRegisteredSubscriberException() {
    }

    public NotRegisteredSubscriberException(String message) {
        super(message);
    }

    public NotRegisteredSubscriberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotRegisteredSubscriberException(Throwable cause) {
        super(cause);
    }

    public NotRegisteredSubscriberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
