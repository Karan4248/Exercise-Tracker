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

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Point> getPoints() { return new ArrayList<>(points); }
    public Exercise getExercise() { return exercise; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getDistance() { return distance; }

    public static void resetIdCounter() {
        nextId = 1;
    }
}
