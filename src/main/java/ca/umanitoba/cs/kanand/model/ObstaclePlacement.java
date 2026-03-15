package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

/**
 * Represents an obstacle placed at a specific location on the grid.
 * Invariant: location != null, type != null
 * Precondition: The obstacle location must be within the grid bounds (0 <= x < gridWidth, 0 <= y < gridHeight)
 */
public class ObstaclePlacement {
    private static int nextId = 1;

    private final int id;
    private final Point location;
    private final Obstacle type;

    /**
     * Creates an obstacle placement at a specific point.
     *
     * @param location the point where the obstacle is placed
     * @param type the type of obstacle
     * @throws NullPointerException if location or type is null
     */
    public ObstaclePlacement(Point location, Obstacle type) {
        this.location = Preconditions.checkNotNull(location, "Precondition failed: location cannot be null");
        this.type = Preconditions.checkNotNull(type, "Precondition failed: type cannot be null");
        
        this.id = nextId++;
        
        Preconditions.checkState(checkInvariant(), "Invariant violated: location and type cannot be null");
    }

    /**
     * Gets the unique ID of this obstacle.
     *
     * @return the obstacle ID
     */
    public int getId() { return id; }

    /**
     * Gets the location of this obstacle.
     *
     * @return the location Point
     */
    public Point getLocation() { return location; }

    /**
     * Gets the type of obstacle.
     *
     * @return the Obstacle type
     */
    public Obstacle getType() { return type; }

    /**
     * Checks if this obstacle occupies the given point.
     *
     * @param point the point to check
     * @return true if the obstacle is at this exact point, false otherwise
     * @throws NullPointerException if point is null
     */
    public boolean occupies(Point point) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");
        
        return point.getX() == location.getX() && point.getY() == location.getY();
    }

    /**
     * Returns a string representation of this obstacle placement.
     *
     * @return a string describing the obstacle
     */
    @Override
    public String toString() {
        return type + " at " + location;
    }

    /**
     * Checks the class invariant.
     *
     * @return true if the invariant is satisfied, false otherwise
     */
    private boolean checkInvariant() {
        return location != null && type != null;
    }

    /**
     * Resets the ID counter to 1. Used for testing and initialization.
     */
    public static void resetIdCounter() {
        nextId = 1;
    }
}