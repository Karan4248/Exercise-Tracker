package ca.umanitoba.cs.kanand.model;

public final class ObstaclePlacementPrinter {
    /**
     * Prevents instantiation of this utility class.
     */
    private ObstaclePlacementPrinter() { }

    /**
     * Formats an ObstaclePlacement into a readable string representation.
     *
     * @param obstacle the ObstaclePlacement to format
     * @return a formatted string with obstacle details
     */
    public static String format(ObstaclePlacement obstacle) {
        if (obstacle == null) return "<null obstacle>";
        return String.format(
                "Obstacle #%d: %s at %s",
                obstacle.getId(),
                obstacle.getType(),
                PointPrinter.format(obstacle.getLocation())
        );
    }
}
