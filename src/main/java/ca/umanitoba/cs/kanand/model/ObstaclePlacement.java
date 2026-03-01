package ca.umanitoba.cs.kanand.model;

/**
 * Represents an obstacle placed at a specific location on the grid.
 * Invariant: location != null, type != null, width > 0, height > 0
 */
public class ObstaclePlacement {
    private static int nextId = 1;

    private final int id;
    private final Point location; // top-left corner
    private final int width;
    private final int height;
    private final Obstacle type;

    /**
     * Creates an obstacle placement with a unique ID.
     *
     * @param location the top-left corner of the obstacle
     * @param width the width of the obstacle (must be positive)
     * @param height the height of the obstacle (must be positive)
     * @param type the type of obstacle
     * @throws IllegalArgumentException if parameters are invalid
     */
    public ObstaclePlacement(Point location, int width, int height, Obstacle type) {
        if (location == null || type == null) {
            throw new IllegalArgumentException("Location and type cannot be null");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.id = nextId++;
        this.location = location;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /**
     * Gets the unique ID of this obstacle.
     *
     * @return the obstacle ID
     */
    public int getId() { return id; }

    /**
     * Gets the top-left corner location of this obstacle.
     *
     * @return the location Point
     */
    public Point getLocation() { return location; }

    /**
     * Gets the width of this obstacle.
     *
     * @return the width
     */
    public int getWidth() { return width; }

    /**
     * Gets the height of this obstacle.
     *
     * @return the height
     */
    public int getHeight() { return height; }

    /**
     * Gets the type of obstacle.
     *
     * @return the Obstacle type
     */
    public Obstacle getType() { return type; }

    /**
     * Checks if this obstacle occupies the given point.
     */
    public boolean occupies(Point point) {
        int px = point.getX();
        int py = point.getY();
        int ox = location.getX();
        int oy = location.getY();
        return px >= ox && px < ox + width && py >= oy && py < oy + height;
    }

    // ... existing code ...

    /**
     * Resets the ID counter to 1. Used for testing and initialization.
     */
    public static void resetIdCounter() {
        nextId = 1;
    }
}