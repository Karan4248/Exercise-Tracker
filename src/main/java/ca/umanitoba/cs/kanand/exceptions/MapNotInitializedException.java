package ca.umanitoba.cs.kanand.exceptions;

/**
 * Thrown when an operation requires the map/grid to be initialized but it has not been.
 * This exception prevents operations on a null grid.
 */
public class MapNotInitializedException extends Exception {
    /**
     * Constructs a MapNotInitializedException with a detail message.
     *
     * @param message the detail message
     */
    public MapNotInitializedException(String message) {
        super(message);
    }

    /**
     * Constructs a MapNotInitializedException with a detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public MapNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
