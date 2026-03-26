package ca.umanitoba.cs.kanand.exceptions;

/**
 * Thrown when a user attempts to create an account with a username that already exists.
 * This exception prevents duplicate usernames in the system.
 */
public class UsernameTakenException extends Exception {
    /**
     * Constructs a UsernameTakenException with a detail message.
     *
     * @param message the detail message explaining that the username is taken
     */
    public UsernameTakenException(String message) {
        super(message);
    }

    /**
     * Constructs a UsernameTakenException with a detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public UsernameTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
