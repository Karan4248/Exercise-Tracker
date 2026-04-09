package ca.umanitoba.cs.kanand.exceptions;

/**
 * Thrown when user provides invalid login credentials.
 * This exception communicates authentication failures to the UI layer.
 */
public class InvalidCredentialsException extends Exception {
    /**
     * Constructs an InvalidCredentialsException with a detail message.
     *
     * @param message the detail message explaining why credentials are invalid
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
