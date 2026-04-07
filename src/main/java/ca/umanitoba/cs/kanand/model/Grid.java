package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the map grid for tracking activities.
 * Invariant: width > 0, height > 0, obstacles != null, coveredPoints != null
 * 
 * The grid maintains a set of covered points for use in pathfinding algorithms.
 * Coverage is tracked to enable finding paths through previously traveled routes.
 */
public class Grid {
    private final int width;
    private final int height;
    private final List<ObstaclePlacement> obstacles;
    private final Set<Point> coveredPoints;

    /**
     * Creates a grid with the specified dimensions.
     *
     * @param width the grid width (must be positive)
     * @param height the grid height (must be positive)
     * @throws IllegalArgumentException if width or height is not positive
     */
    public Grid(int width, int height) {
        Preconditions.checkArgument(width > 0, "Precondition failed: width must be positive");
        Preconditions.checkArgument(height > 0, "Precondition failed: height must be positive");

        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();
        this.coveredPoints = new HashSet<>();

        Preconditions.checkState(checkInvariant(), "Invariant violated");
    }

    /**
     * Gets the width of the grid.
     *
     * @return the width
     */
    public int getWidth() { return width; }

    /**
     * Gets the height of the grid.
     *
     * @return the height
     */
    public int getHeight() { return height; }

    /**
     * Gets a copy of all obstacles on the grid.
     *
     * @return a copy of the obstacles list
     */
    public List<ObstaclePlacement> getObstacles() {
        return new ArrayList<>(obstacles);
    }

    /**
     * Checks if a point is valid (in bounds and not occupied by an obstacle).
     *
     * @param point the Point to check
     * @return true if the point is valid, false otherwise
     * @throws NullPointerException if point is null
     */
    public boolean isValid(Point point) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");

        if (point.getX() < 0 || point.getX() > width ||
            point.getY() < 0 || point.getY() > height) {
            return false;
        }
        for (ObstaclePlacement obs : obstacles) {
            if (obs.occupies(point)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a point is within the grid bounds.
     *
     * @param point the Point to check
     * @return true if the point is in bounds, false otherwise
     * @throws NullPointerException if point is null
     */
    public boolean isInBounds(Point point) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");

        return point.getX() >= 0 && point.getX() <= width &&
               point.getY() >= 0 && point.getY() <= height;
    }

    /**
     * Adds an obstacle to the grid.
     *
     * @param obstacle the ObstaclePlacement to add
     * @throws NullPointerException if obstacle is null
     * @throws IllegalArgumentException if the obstacle location is out of bounds
     */
    public void addObstacle(ObstaclePlacement obstacle) {
        Preconditions.checkNotNull(obstacle, "Precondition failed: obstacle cannot be null");
        
        Point location = obstacle.getLocation();
        Preconditions.checkArgument(isInBounds(location), 
            "Precondition failed: obstacle location must be within grid bounds");

        obstacles.add(obstacle);

        Preconditions.checkState(obstacles.contains(obstacle), "Postcondition failed: obstacle not added");
        Preconditions.checkState(checkInvariant(), "Invariant violated");
    }

    /**
     * Removes an obstacle from the grid by its ID.
     *
     * @param id the ID of the obstacle to remove
     * @return true if an obstacle was removed, false otherwise
     */
    public boolean removeObstacle(int id) {
        boolean removed = obstacles.removeIf(obs -> obs.getId() == id);

        Preconditions.checkState(checkInvariant(), "Invariant violated");
        return removed;
    }

    /**
     * Retrieves the obstacle at a specific point.
     *
     * @param point the Point to check
     * @return the ObstaclePlacement at that point, or null if none
     * @throws NullPointerException if point is null
     */
    public ObstaclePlacement getObstacleAt(Point point) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");

        for (ObstaclePlacement obs : obstacles) {
            if (obs.occupies(point)) {
                return obs;
            }
        }
        return null;
    }

    /**
     * Adds a point to the set of covered points (points that have been traversed by routes).
     * Used by the pathfinding algorithm to find valid routes.
     * 
     * Precondition:
     *   - point != null
     *   - point must be in bounds
     * 
     * Postcondition:
     *   - point is added to the covered points set
     *   - isCovered(point) returns true
     * 
     * @param point the Point that has been covered
     * @throws NullPointerException if point is null
     * @throws IllegalArgumentException if point is out of bounds
     */
    public void addCoveredPoint(Point point) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");
        Preconditions.checkArgument(isInBounds(point), 
            "Precondition failed: covered point must be within grid bounds");

        coveredPoints.add(point);

        Preconditions.checkState(checkInvariant(), "Invariant violated after addCoveredPoint");
        Preconditions.checkState(isCovered(point), "Postcondition failed: point not marked as covered");
    }

    /**
     * Checks if a point is covered (has been traversed by a previous route).
     * 
     * Precondition:
     *   - point != null
     * 
     * Postcondition:
     *   - coveredPoints set is unchanged
     *   - Returns true if point is in the covered set, false otherwise
     * 
     * @param point the Point to check
     * @return true if the point has been covered, false otherwise
     * @throws NullPointerException if point is null
     */
    public boolean isCovered(Point point) {
        Preconditions.checkNotNull(point, "Precondition failed: point cannot be null");
        Preconditions.checkState(checkInvariant(), "Invariant violated before isCovered");

        return coveredPoints.contains(point);
    }

    /**
     * Gets a copy of all covered points on the grid.
     * 
     * Postcondition:
     *   - The returned set is a copy (modifications don't affect the grid)
     *   - The returned set contains all covered points
     * 
     * @return a copy of the covered points set
     */
    public Set<Point> getCoveredPoints() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before getCoveredPoints");
        return new HashSet<>(coveredPoints);
    }

    /**
     * Clears all covered points from the grid.
     * 
     * Postcondition:
     *   - coveredPoints is empty
     *   - isCovered(point) returns false for all points
     */
    public void clearCoveredPoints() {
        coveredPoints.clear();

        Preconditions.checkState(checkInvariant(), "Invariant violated after clearCoveredPoints");
        Preconditions.checkState(coveredPoints.isEmpty(), "Postcondition failed: covered points not cleared");
    }

    /**
     * Checks if the grid maintains its invariant conditions.
     *
     * @return true if all invariants hold, false otherwise
     */
    private boolean checkInvariant() {
        return width > 0 && height > 0 && obstacles != null && coveredPoints != null;
    }

    /**
     * Creates a new builder for Grid objects.
     *
     * @return a new GridBuilder instance
     */
    public static GridBuilder builder() {
        return new GridBuilder();
    }

    /**
     * Builder class for constructing Grid objects.
     * Follows the Builder pattern to separate construction logic from domain logic.
     */
    public static class GridBuilder {
        private int width;
        private int height;

        /**
         * Sets the width of the grid.
         *
         * @param width the grid width (must be positive)
         * @return this builder instance for method chaining
         */
        public GridBuilder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * Sets the height of the grid.
         *
         * @param height the grid height (must be positive)
         * @return this builder instance for method chaining
         */
        public GridBuilder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * Builds and returns a new Grid instance with the configured dimensions.
         *
         * @return a new Grid object
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Grid build() {
            return new Grid(this.width, this.height);
        }
    }
}
