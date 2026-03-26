package ca.umanitoba.cs.kanand.exceptions;

/**
 * Thrown when a point is invalid for the current context (e.g., out of bounds, occupied by obstacle).
 * This exception helps communicate coordinate validation errors from domain model to layers above.
 */
public class InvalidPointException extends Exception {
    /**
     * Constructs an InvalidPointException with a detail message.
     *
     * @param message the detail message explaining why the point is invalid
     */
    public InvalidPointException(String message) {
        super(message);
    }

    /**
     * Constructs an InvalidPointException with a detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public InvalidPointException(String message, Throwable cause) {
        super(message, cause);
    }
}
