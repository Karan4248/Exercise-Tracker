package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

/**
 * Represents a coordinate point on the grid.
 * Invariant: x >= 0 and y >= 0
 */
public class Point {
    private final int x;
    private final int y;

    /**
     * Creates a Point with the specified coordinates.
     *
     * @param x the X coordinate (must be non-negative)
     * @param y the Y coordinate (must be non-negative)
     */
    public Point(int x, int y) {
        Preconditions.checkState(x >= 0, "Invariant failed: x must be >= 0");
        Preconditions.checkState(y >= 0, "Invariant failed: y must be >= 0");
        this.x = x;
        this.y = y;

        Preconditions.checkState(this.x >= 0 && this.y >= 0,
                "Invariant violated: coordinates must be non-negative");
    }

    /**
     * Gets the X coordinate of this point.
     *
     * @return the X coordinate
     */
    public int getX() { return x; }

    /**
     * Gets the Y coordinate of this point.
     *
     * @return the Y coordinate
     */
    public int getY() { return y; }

    /**
     * Compares this point with another object for equality.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    /**
     * Generates a hash code for this point.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    // ... existing code ...
}