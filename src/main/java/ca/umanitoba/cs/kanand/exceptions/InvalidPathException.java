package ca.umanitoba.cs.kanand.exceptions;

/**
 * Thrown when the path-finding algorithm cannot find a valid path between two points.
 * This indicates that no route exists given the current map constraints.
 */
public class InvalidPathException extends Exception {
    /**
     * Constructs an InvalidPathException with a detail message.
     *
     * @param message the detail message explaining why no path could be found
     */
    public InvalidPathException(String message) {
        super(message);
    }
}
