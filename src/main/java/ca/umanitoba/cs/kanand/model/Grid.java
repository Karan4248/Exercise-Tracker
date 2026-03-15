package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the map grid for tracking activities.
 * Invariant: width > 0, height > 0, obstacles != null
 */
public class Grid {
    private final int width;
    private final int height;
    private final List<ObstaclePlacement> obstacles;

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
     * Checks if the grid maintains its invariant conditions.
     *
     * @return true if all invariants hold, false otherwise
     */
    private boolean checkInvariant() {
        return width > 0 && height > 0 && obstacles != null;
    }
}
