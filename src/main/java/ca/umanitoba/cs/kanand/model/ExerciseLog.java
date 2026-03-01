package ca.umanitoba.cs.kanand.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single exercise activity with a route on the grid.
 * Invariant: points != null, exercise != null, id > 0
 */
public class ExerciseLog {
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final List<Point> points;
    private final Exercise exercise;
    private final LocalDateTime timestamp;
    private final double distance;

    /**
     * Creates a new exercise log entry with a unique ID.
     *
     * @param name the activity name (non-empty)
     * @param exercise the Exercise type
     * @param route the list of route points (non-empty)
     * @param distance the total distance covered
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public ExerciseLog(String name, Exercise exercise, List<Point> route, double distance) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty");
        }
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise cannot be null");
        }
        if (route == null || route.isEmpty()) {
            throw new IllegalArgumentException("Route cannot be null or empty");
        }
        this.id = nextId++;
        this.name = name;
        this.exercise = exercise;
        this.points = new ArrayList<>(route);
        this.timestamp = LocalDateTime.now();
        this.distance = distance;
    }

    /**
     * Gets the unique ID of this exercise log.
     *
     * @return the exercise log ID
     */
    public int getId() { return id; }
    /**
     * Gets the name of the activity.
     *
     * @return the activity name
     */
    public String getName() { return name; }

    /**
     * Gets a copy of the route points.
     *
     * @return a copy of the points list
     */
    public List<Point> getPoints() { return new ArrayList<>(points); }

    /**
     * Gets the exercise type for this log.
     *
     * @return the Exercise
     */
    public Exercise getExercise() { return exercise; }

    /**
     * Gets the timestamp when this activity was recorded.
     *
     * @return the LocalDateTime
     */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Gets the total distance covered in this activity.
     *
     * @return the distance
     */
    public double getDistance() { return distance; }

    /**
     * Resets the ID counter to 1. Used for testing and initialization.
     */
    public static void resetIdCounter() {
        nextId = 1;
    }
}
