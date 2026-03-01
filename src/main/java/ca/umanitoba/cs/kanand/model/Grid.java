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

    public Grid(int width, int height) {
        assert width > 0 : "Precondition failed: width must be positive";
        assert height > 0 : "Precondition failed: height must be positive";

        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();

        assert checkInvariant() : "Invariant violated";
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public List<ObstaclePlacement> getObstacles() {
        return new ArrayList<>(obstacles);
    }

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

    public boolean isInBounds(Point point) {
        assert point != null : "Precondition failed: point cannot be null";

        return point.getX() >= 0 && point.getX() < width &&
               point.getY() >= 0 && point.getY() < height;
    }

    public void addObstacle(ObstaclePlacement obstacle) {
        assert obstacle != null : "Precondition failed: obstacle cannot be null";

        obstacles.add(obstacle);

        assert obstacles.contains(obstacle) : "Postcondition failed: obstacle not added";
        assert checkInvariant() : "Invariant violated";
    }

    public boolean removeObstacle(int id) {
        boolean removed = obstacles.removeIf(obs -> obs.getId() == id);

        assert checkInvariant() : "Invariant violated";
        return removed;
    }

    public ObstaclePlacement getObstacleAt(Point point) {
        assert point != null : "Precondition failed: point cannot be null";

        for (ObstaclePlacement obs : obstacles) {
            if (obs.occupies(point)) {
                return obs;
            }
        }
        return null;
    }

    private boolean checkInvariant() {
        return width > 0 && height > 0 && obstacles != null;
    }
}
