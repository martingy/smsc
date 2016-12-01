package com.meltwater.smsc.exceptions;

/**
 * thrown when a Subscriber doesn't exist
 */
public class NotExistingSubscriberException extends Exception {

    public NotExistingSubscriberException() {
    }

    public NotExistingSubscriberException(String message) {
        super(message);
    }

    public NotExistingSubscriberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingSubscriberException(Throwable cause) {
        super(cause);
    }

    public NotExistingSubscriberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
