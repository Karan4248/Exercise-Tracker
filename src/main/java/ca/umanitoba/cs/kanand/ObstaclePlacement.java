package ca.umanitoba.cs.kanand;

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

    public int getId() { return id; }
    public Point getLocation() { return location; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
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

    @Override
    public String toString() {
        return String.format("Obstacle #%d: %s at %s, size %dx%d",
                id, type.getDisplayName(), location, width, height);
    }

    public static void resetIdCounter() {
        nextId = 1;
    }
}
