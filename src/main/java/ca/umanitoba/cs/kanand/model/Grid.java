package ca.umanitoba.cs.kanand.model;

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
     */
    public Grid(int width, int height) {
        assert width > 0 : "Precondition failed: width must be positive";
        assert height > 0 : "Precondition failed: height must be positive";

        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();

        assert checkInvariant() : "Invariant violated";
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
     */
    public boolean isValid(Point point) {
        assert point != null : "Precondition failed: point cannot be null";

        if (point.getX() < 0 || point.getX() >= width ||
            point.getY() < 0 || point.getY() >= height) {
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
     */
    public boolean isInBounds(Point point) {
        assert point != null : "Precondition failed: point cannot be null";

        return point.getX() >= 0 && point.getX() < width &&
               point.getY() >= 0 && point.getY() < height;
    }

    /**
     * Adds an obstacle to the grid.
     *
     * @param obstacle the ObstaclePlacement to add
     */
    public void addObstacle(ObstaclePlacement obstacle) {
        assert obstacle != null : "Precondition failed: obstacle cannot be null";

        obstacles.add(obstacle);

        assert obstacles.contains(obstacle) : "Postcondition failed: obstacle not added";
        assert checkInvariant() : "Invariant violated";
    }

    /**
     * Removes an obstacle from the grid by its ID.
     *
     * @param id the ID of the obstacle to remove
     * @return true if an obstacle was removed, false otherwise
     */
    public boolean removeObstacle(int id) {
        boolean removed = obstacles.removeIf(obs -> obs.getId() == id);

        assert checkInvariant() : "Invariant violated";
        return removed;
    }

    /**
     * Retrieves the obstacle at a specific point.
     *
     * @param point the Point to check
     * @return the ObstaclePlacement at that point, or null if none
     */
    public ObstaclePlacement getObstacleAt(Point point) {
        assert point != null : "Precondition failed: point cannot be null";

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
