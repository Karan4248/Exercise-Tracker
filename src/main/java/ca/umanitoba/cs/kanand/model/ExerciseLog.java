package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single exercise activity with a route on the grid.
 * Invariant: points != null, exercise != null, id > 0, name != null
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
     * @throws NullPointerException if name, exercise, or route is null
     * @throws IllegalStateException if name or route is empty
     */
    public ExerciseLog(String name, Exercise exercise, List<Point> route, double distance) {
        this.name = Preconditions.checkNotNull(name, "Precondition failed: name cannot be null");
        Preconditions.checkState(!this.name.isEmpty(), "Precondition failed: name cannot be empty");
        
        this.exercise = Preconditions.checkNotNull(exercise, "Precondition failed: exercise cannot be null");
        
        Preconditions.checkNotNull(route, "Precondition failed: route cannot be null");
        Preconditions.checkState(!route.isEmpty(), "Precondition failed: route cannot be empty");
        
        this.id = nextId++;
        this.points = new ArrayList<>(route);
        this.timestamp = LocalDateTime.now();
        this.distance = distance;
        
        Preconditions.checkState(checkInvariant(), "Invariant violated: required fields cannot be null");
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
     * Checks the class invariant.
     *
     * @return true if the invariant is satisfied, false otherwise
     */
    private boolean checkInvariant() {
        return name != null && !name.isEmpty() && 
               exercise != null && 
               points != null && !points.isEmpty() && 
               id > 0;
    }

    /**
     * Resets the ID counter to 1. Used for testing and initialization.
     */
    public static void resetIdCounter() {
        nextId = 1;
    }
}

