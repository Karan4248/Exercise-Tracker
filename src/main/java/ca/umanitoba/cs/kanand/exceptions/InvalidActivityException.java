package ca.umanitoba.cs.kanand.exceptions;

/**
 * Thrown when an operation on an activity is invalid.
 * Examples: adding activity with empty route, duplicate activity names, etc.
 */
public class InvalidActivityException extends Exception {
    /**
     * Constructs an InvalidActivityException with a detail message.
     *
     * @param message the detail message explaining why the activity is invalid
     */
    public InvalidActivityException(String message) {
        super(message);
    }

    /**
     * Constructs an InvalidActivityException with a detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public InvalidActivityException(String message, Throwable cause) {
        super(message, cause);
    }
}
