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
        Preconditions.checkState(x >= 0, "Precondition failed: x must be >= 0");
        Preconditions.checkState(y >= 0, "Precondition failed: y must be >= 0");
        this.x = x;
        this.y = y;
        Preconditions.checkState(checkInvariant(), "Invariant violated");
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

    /**
     * Checks the class invariant.
     *
     * @return true if the invariant is satisfied, false otherwise
     */
    private boolean checkInvariant() {
        return x >= 0 && y >= 0;
    }

    /**
     * Creates a new builder for Point objects.
     *
     * @return a new PointBuilder instance
     */
    public static PointBuilder builder() {
        return new PointBuilder();
    }

    /**
     * Builder class for constructing Point objects.
     * Follows the Builder pattern to separate construction logic from domain logic.
     */
    public static class PointBuilder {
        private int x;
        private int y;

        /**
         * Sets the X coordinate.
         *
         * @param x the X coordinate (must be non-negative)
         * @return this builder instance for method chaining
         */
        public PointBuilder x(int x) {
            this.x = x;
            return this;
        }

        /**
         * Sets the Y coordinate.
         *
         * @param y the Y coordinate (must be non-negative)
         * @return this builder instance for method chaining
         */
        public PointBuilder y(int y) {
            this.y = y;
            return this;
        }

        /**
         * Builds and returns a new Point instance with the configured coordinates.
         *
         * @return a new Point object
         * @throws IllegalStateException if x or y is negative
         */
        public Point build() {
            return new Point(this.x, this.y);
        }
    }
}