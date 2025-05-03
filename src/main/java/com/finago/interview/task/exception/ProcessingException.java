package com.finago.interview.task.exception;

/**
 * Custom exception thrown when processing of a receiver fails.
 */
public class ProcessingException extends Exception {

    public ProcessingException(final String message) {
        super(message);
    }

    public ProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
